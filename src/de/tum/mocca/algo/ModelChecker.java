/* 
 * ModelChecker.java - Copyright 2004 Johannes Kinder <jk@jakstab.org>
 * This file is part of the Mocca project.
 */
package de.tum.mocca.algo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org._3pq.jgrapht.edge.DirectedEdge;
import org.apache.log4j.Logger;

import de.tum.mocca.Specification;
import de.tum.mocca.ctpl.CtplOperator;
import de.tum.mocca.ctpl.CtplPredicate;
import de.tum.mocca.ctpl.CtplQuantifier;
import de.tum.mocca.ctpl.CtplTreeNode;

/**
 * A model checker for assembler functions using unification
 * for predicate checking. 
 *
 * @author Johannes Kinder
 */
public class ModelChecker {

	public static final String P_LOCATION = "#loc";
	
	private static Logger log = Logger.getLogger(ModelChecker.class.getName());
	
	/**
	 * Checks if the CTPL specification holds for the entrypoint of
	 * kripke structure k. 
	 * @param k Kripke Structure
	 * @param spec Specification to check
	 * @return True when the formula holds, else false. 
	 */
	public static boolean check(KripkeStructure k, Specification spec) {

		List formulas = spec.getSubformulaList();
		log.info("======= Modelchecker Start =======");
		long startTime = System.currentTimeMillis();
		
		/* iterate all subformulas */
		for (Iterator fIter = formulas.iterator(); fIter.hasNext();) {
			
			CtplTreeNode f = (CtplTreeNode)fIter.next();
			
			/* 
			 * AF psi
			 */
			if (f.getValue().equals(CtplOperator.AF)) {
				CtplTreeNode psi = f.getChild(0);
				boolean changed = false;
				System.out.print("Checking AF, Iterations:");

				// If any state is labelled with psi
				// then label this state with AF psi.
				for (Iterator iter = k.getGraph().vertexSet().iterator(); iter.hasNext();) {
					KripkeNode s = (KripkeNode)iter.next();
					if (s.hasLabel(psi))
						changed |= s.addLabel(f, (BindingSet)s.getBindingSet(psi).clone());							
				}

				// Fixed point algorithm
				int iterations = 0;
				while (changed) {
					System.out.print(" " + ++iterations);
					changed = false;
					stateIteration: for (Iterator iter = k.getGraph().vertexSet().iterator();
					iter.hasNext();) {
						KripkeNode s = (KripkeNode)iter.next();
						if (k.getGraph().outDegreeOf(s) < 1) continue;
						BindingSet join = null;
						// boolean isAF = true;
						for (Iterator cIter = k.getGraph().outgoingEdgesOf(s).iterator(); cIter.hasNext();) {
							KripkeNode child = (KripkeNode)((DirectedEdge)(cIter.next())).getTarget(); 
							if (!child.hasLabel(f)) continue stateIteration;
							else {
								if (join == null) join = child.getBindingSet(f);
								else join = BindingSet.and(child.getBindingSet(f), join);
								if (join.isEmpty()) continue stateIteration;
							}
						}
						changed |= s.addLabel(f, join); 
					}
				}
				System.out.println(" Done.");
			}

			/* 
			 * EF(phi)
			 */
			else if (f.getValue().equals(CtplOperator.EF)) {

				CtplTreeNode phi = f.getChild(0);
				boolean changed = false;
				System.out.print("Checking EF, Iterations:");
				
				// If any state is labelled with the formula f
				// within EF, then label this state with EF(f).
				for (Iterator iter = k.getGraph().vertexSet().iterator(); iter.hasNext();) {
					KripkeNode s = (KripkeNode)iter.next();
					if (s.hasLabel(phi))
						changed |= s.addLabel(f, (BindingSet)s.getBindingSet(phi).clone());
				}

				// Fixed point algorithm
				int iterations = 0;
				while (changed) {
					System.out.print(" " + ++iterations);
					k.clearMarks();
					changed = false;
					LinkedList queue = new LinkedList();
					queue.add(k.getExitNode());
					while(!queue.isEmpty()) {
						/* Breadth first search */
						KripkeNode s = (KripkeNode)queue.removeFirst();
						s.mark(); // Make sure to only access every node once.

						/* Iterate parents */
						// TODO: No support for procedures with multiple return statements.
						for (Iterator iter = k.getGraph().incomingEdgesOf(s).iterator(); iter.hasNext();) {
							KripkeNode parent = (KripkeNode)((DirectedEdge)(iter.next())).getSource(); 
							/* parent propagation goes here */
							if (s.hasLabel(f)) changed |= parent.addLabel(f, (BindingSet)s.getBindingSet(f).clone());
							if (!parent.isMarked()) queue.add(parent);
						}
					}
				}
				System.out.println(" Done.");
			}
			/* 
			 * E phi U psi
			 */
			else if (f.getValue().equals(CtplOperator.EU)) {
				CtplTreeNode phi = f.getChild(0);
				CtplTreeNode psi = f.getChild(1);
				boolean changed = false;
				System.out.print("Checking EU, Iterations:");

				// If any state is labelled with the formula psi
				// within E phi U psi, then label this state with E phi U psi.
				for (Iterator iter = k.getGraph().vertexSet().iterator(); iter.hasNext();) {
					KripkeNode s = (KripkeNode)iter.next();
					if (s.hasLabel(psi))
						changed |= s.addLabel(f, (BindingSet)s.getBindingSet(psi).clone());							
				}

				// Fixed point algorithm
				int iterations = 0;
				while (changed) {
					System.out.print(" " + ++iterations);
					k.clearMarks();
					changed = false;
					LinkedList queue = new LinkedList();
					queue.add(k.getExitNode());
					while(!queue.isEmpty()) {
						/* Breadth first search */
						KripkeNode s = (KripkeNode)queue.removeFirst();
						s.mark(); // Make sure to only access every node once.

						/* Iterate parents */
						// TODO: No support for procedures with multiple return statements.
						for (Iterator iter = k.getGraph().incomingEdgesOf(s).iterator(); iter.hasNext();) {
							KripkeNode parent = (KripkeNode)((DirectedEdge)(iter.next())).getSource(); 
							/* parent propagation goes here */
							if (s.hasLabel(f) 
									&& parent.hasLabel(phi)) {
								BindingSet join = BindingSet.and(s.getBindingSet(f), parent.getBindingSet(phi));
								if (!join.isEmpty()) changed |= parent.addLabel(f, join); 
							}
							if (!parent.isMarked()) queue.add(parent);
						}
					}
				}
				System.out.println(" Done.");
			}
			/*
			 * EX
			 */
			else if (f.getValue().equals(CtplOperator.EX)) {
				for (Iterator iter = k.getGraph().vertexSet().iterator();
				iter.hasNext();) {
					KripkeNode s = (KripkeNode)iter.next();
					if (s.hasLabel(f.getChild(0)))
						for (Iterator iter2 = k.getGraph().incomingEdgesOf(s).iterator(); iter2.hasNext();) {
							KripkeNode parent = (KripkeNode)((DirectedEdge)(iter2.next())).getSource(); 
							parent.addLabel(f, (BindingSet)s.getBindingSet(f.getChild(0)).clone()); 
						}
				}
			}
			
			/* 
			 * AND
			 */
			else if (f.getValue().equals(CtplOperator.AND)) {
				//System.out.println(f.getChild(1).toString());
				for (Iterator iter = k.getGraph().vertexSet().iterator();
				iter.hasNext();) {
					KripkeNode s = (KripkeNode)iter.next();
					if (s.hasLabel(f.getChild(0))
						&& s.hasLabel(f.getChild(1))) {
						 BindingSet join = BindingSet.and(s.getBindingSet(f.getChild(0)), 
						 		s.getBindingSet(f.getChild(1)));
						 if (!join.isEmpty()) s.setLabel(f, join); 
					}
				}
			}
			/* 
			 * OR
			 */
			else if (f.getValue().equals(CtplOperator.OR)) {
				for (Iterator iter = k.getGraph().vertexSet().iterator();
				iter.hasNext();) {
					KripkeNode s = (KripkeNode)iter.next();
					if (s.hasLabel(f.getChild(0))) {
						BindingSet c = s.getBindingSet(f.getChild(0));
						if (s.hasLabel(f.getChild(1))) c = BindingSet.or(c, s.getBindingSet(f.getChild(1)));
						s.setLabel(f, c);
					}
					else if (s.hasLabel(f.getChild(1)))
						s.setLabel(f, s.getBindingSet(f.getChild(1)));
				}
			} 
			/* 
			 * NOT
			 */
			else if (f.getValue().equals(CtplOperator.NOT)) {
				for (Iterator iter = k.getGraph().vertexSet().iterator();
				iter.hasNext();) {
					KripkeNode s = (KripkeNode)iter.next();
					if (!s.hasLabel(f.getChild(0))) {
						BindingSet c = new BindingSet();
						c.add(BindingClause.emptyClause);
						s.setLabel(f, c);
					}
					else {
						BindingSet res = BindingSet.negate(s.getBindingSet(f.getChild(0)));
						if (!res.isEmpty()) s.setLabel(f, res);
					}
				}
			}
			/*
			 * EXISTS QUANTIFIER
			 */
			else if (f.getValue().equals(CtplQuantifier.EXISTS)) {
				for (Iterator iter = k.getGraph().vertexSet().iterator();
				iter.hasNext();) {
					KripkeNode s = (KripkeNode)iter.next();
					if (s.hasLabel(f.getChild(0))) {
						s.setLabel(f, BindingSet.purge(
								s.getBindingSet(f.getChild(0)),
								((CtplQuantifier)f.getValue()).getParameter())
								);
					}
				}				
			}
			/* 
			 * PREDICATE
			 */
			else {
				/* location predicates are no instructions, but special */
				boolean locOnly;
				if (((CtplPredicate)f.getValue()).getBody().equals(P_LOCATION)) 
					locOnly = true; 
				else locOnly = false;
				for (Iterator iter = k.getGraph().vertexSet().iterator();
				iter.hasNext();) {
					KripkeNode s = (KripkeNode)iter.next();
					BindingClause b;
					if (locOnly) 
						b = s.getInstruction().getLocation().unify((CtplPredicate)f.getValue());
					else
						b = s.getInstruction().unify((CtplPredicate)f.getValue());
					if (b != null) {
						BindingSet c = new BindingSet();
						c.add(b);
						if (locOnly) s.addLabel(f, c);
						else s.setLabel(f, c);
					}
				}
			}
		}

		long stopTime = System.currentTimeMillis();
		log.info("======= Modelchecker Stop =======");
		log.info("Finished after " + (stopTime - startTime) + "ms.");
		
		return k.getEntryNode().hasLabel((CtplTreeNode)formulas.get(formulas.size() - 1));
	}	
}
