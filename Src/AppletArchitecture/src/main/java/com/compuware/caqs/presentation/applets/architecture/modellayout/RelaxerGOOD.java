package com.compuware.caqs.presentation.applets.architecture.modellayout;

import java.util.Enumeration;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.domain.architecture.serializeddata.Edge;
import com.compuware.caqs.domain.architecture.serializeddata.Node;

public class RelaxerGOOD {
	/*
	 * Duplicate of the spring algorithm for test
	 */
	static public void springRelax(ArchitectureModel model) {
		// springRelaxGoodOldVersion(model);
		gravity(model);
	}

	static public void circleRelax(ArchitectureModel model) {
		// move nodes and avoid graph limits
		Enumeration nodesEnum3 = model.getNodeList().elements();
		double t = 0;
		int numberOfUndeletedNodes = (model.getNodeList().size() - model
				.getNumberOfDeletedNodes());
		if (numberOfUndeletedNodes > 0) {
			double pas = 2 * Math.PI / numberOfUndeletedNodes;
			while (nodesEnum3.hasMoreElements()) {
				Node n = (Node) nodesEnum3.nextElement();

				int a = 400;
				int b = 400;
				int r = 400;
				if (numberOfUndeletedNodes > 400) {
					a = model.getNodeList().size();
					b = model.getNodeList().size();
					r = model.getNodeList().size();
				}
				if (!n.isAutoLayoutProtected()) {
					n.setX(a + r * Math.cos(t));
					n.setY(b + r * Math.sin(t));
					t = t + pas;

					n.setDx(n.getDx() / 2);
					n.setDy(n.getDy() / 2);
				}
			}
			model.fireModelChangedEvent();
		}
	}

	/*
	 * spring algorithm
	 */
	static public void gravity(ArchitectureModel model) {

		// Edges impact
		Enumeration edgesEnum = model.getEdgesList().elements();
		while (edgesEnum.hasMoreElements()) {
			Edge e = (Edge) edgesEnum.nextElement();
			if (!e.isDeleted()) {
				double vx = e.getTo().getCenterX() - e.getFrom().getCenterX();
				double vy = e.getTo().getCenterY() - e.getFrom().getCenterY();
				double realLength = Math.sqrt(vx * vx + vy * vy);
				realLength = (realLength == 0) ? 0.0001 : realLength;

				
				int edgePreferedLength = 200;
				if (e.isReal()){
					edgePreferedLength = 400;
				}
				double f = (edgePreferedLength - realLength) / (realLength * 3);

				double dx = f * vx;
				double dy = f * vy;

				e.getFrom().setDx(e.getFrom().getDx() - dx);
				e.getFrom().setDy(e.getFrom().getDy() - dy);
				e.getTo().setDx(e.getTo().getDx() + dx);
				e.getTo().setDy(e.getTo().getDy() + dy);
			}
		}

		// the node repulsion
		Enumeration nodesEnum = model.getNodeList().elements();
		while (nodesEnum.hasMoreElements()) {
			Node n1 = (Node) nodesEnum.nextElement();
			if (!n1.isAutoLayoutProtected()) {

				double dx = 0;
				double dy = 0;

				/*
				 * if (n1.getCallers().size() == 0) { dy += -20; } else { if
				 * (n1.getCallers().size() < n1.getCallees().size()) { dy += -5 *
				 * n1.getCallees().size(); } else if (n1.getCallers().size() >
				 * n1.getCallees().size()) { dy += +5 * n1.getCallers().size(); } }
				 */

				// Node repulsion
				Enumeration nodesEnum2 = model.getNodeList().elements();
				while (nodesEnum2.hasMoreElements()) {
					Node n2 = (Node) nodesEnum2.nextElement();
					if (n1 != n2) {
						double vx = n1.getCenterX() - n2.getCenterX();
						double vy = n1.getCenterY() - n2.getCenterY();

						if (n1.getCallees().contains(n2)) {
							if (n1.getCallers().contains(n2)) {
								// should on the same y
								// System.out.println(n1.getLbl()+ " should be
								// on same " + n2.getLbl() );
								switch (n1.relativePosition(n2)) {
								case Node.AL:
									break;
								case Node.AR:
									break;
								case Node.BR:
									break;
								case Node.BL:
									break;
								}
							} else {
								// should be above
								// System.out.println(n1.getLbl()+ " should be
								// above " + n2.getLbl() );
								switch (n1.relativePosition(n2)) {
								case Node.AL:
									// System.out.println(n1.getLbl()+ " is
									// above " + n2.getLbl() );
									break;
								case Node.AR:
									// System.out.println(n1.getLbl()+ " is
									// above " + n2.getLbl() );
									break;
								case Node.BR:
									vy -= 10;
									break;
								case Node.BL:
									vy -= 10;
									break;
								}
							}
						} else if (n1.getCallers().contains(n2)) {
							// should be under
							// System.out.println(n1.getLbl()+ " should be under
							// " + n2.getLbl() );
							switch (n1.relativePosition(n2)) {
							case Node.AL:
								vy += 10;
								break;
							case Node.AR:
								vy += 10;
								break;
							case Node.BR:
								// System.out.println(n1.getLbl()+ " is be under
								// " + n2.getLbl() );
								break;
							case Node.BL:
								// System.out.println(n1.getLbl()+ " is be under
								// " + n2.getLbl() );
								break;
							}
						}

						// else {
						if (n1.overlap(n2)) {
							switch (n1.relativePosition(n2)) {
							case Node.AL:
								vx += n2.getUpperLeftX() - n1.getBottomRightX();
								vy += n2.getUpperLeftY() - n1.getBottomRightY();
								break;
							case Node.AR:
								vx += n2.getUpperRightX() - n1.getBottomLeftX();
								vy += n2.getUpperRightY() - n1.getBottomLeftY();
								break;
							case Node.BR:
								vx += n2.getBottomRightX() - n1.getUpperLeftX();
								vy += n2.getBottomRightY() - n1.getUpperLeftY();
								// System.out.println(n1.getLbl()+ " is be
								// under
								// " + n2.getLbl() );
								break;
							case Node.BL:
								vx += n2.getBottomLeftX() - n1.getUpperRightX();
								vy += n2.getBottomLeftY() - n1.getUpperRightY();
								// System.out.println(n1.getLbl()+ " is be
								// under
								// " + n2.getLbl() );
								break;
							}
						}
						// }
						double realLength = Math.sqrt(vx * vx + vy * vy);

						if (realLength == 0) {
							// dx += Math.random();
							// dy += Math.random();
						} else if (realLength < 200) {
							dx += vx;
							dy += vy;
						} else if (realLength > 600) {
							dx += 0;
							dy += 0;
						} else {
							dx += vx / realLength;
							dy += vy / realLength;
						}

					}
				}
				double dlen = Math.sqrt(dx * dx + dy * dy);

				if (dlen > 0) {
					dlen = Math.sqrt(dlen) / 2;

					n1.setDx(n1.getDx() + dx / dlen);
					n1.setDy(n1.getDy() + dy / dlen);
				}

			}
		}

		// move nodes and avoid graph limits
		Enumeration nodesEnum3 = model.getNodeList().elements();
		while (nodesEnum3.hasMoreElements()) {
			Node n = (Node) nodesEnum3.nextElement();
			if (!n.isAutoLayoutProtected()) {
				double x = n.getCenterX() + n.getDx();
				double y = n.getCenterY() + n.getDy();

				n.setX(x);
				n.setY(y);

				if (n.getUpperLeftX() < 0) {
					if (n.getUpperLeftY() < 0) {
						model.translateAllModel(-n.getUpperLeftX(), -n
								.getUpperLeftY());
					} else {
						model.translateAllModel(-n.getUpperLeftX(), 0.0);
					}
				} else if (n.getUpperLeftY() < 0) {
					model.translateAllModel( 0.0, -n.getUpperLeftY());
				}

				n.setDx(n.getDx() / 2);
				n.setDy(n.getDy() / 2);
			}
		}

		model.fireModelChangedEvent();
	}

	

	

	
	/*
	 * spring algorithm
	 */
	static public void gravityDeep(ArchitectureModel model) {

		// Edges impact
		Enumeration edgesEnum = model.getEdgesList().elements();
		while (edgesEnum.hasMoreElements()) {
			Edge e = (Edge) edgesEnum.nextElement();
			if (!e.isDeleted()) {
				double vx = e.getTo().getCenterX() - e.getFrom().getCenterX();
				double vy = e.getTo().getCenterY() - e.getFrom().getCenterY();
				double realLength = Math.sqrt(vx * vx + vy * vy);
				realLength = (realLength == 0) ? 0.0001 : realLength;

				int edgePreferedLength = 1000;
				if (e.getCouples().size() > 0) {
					edgePreferedLength = 1000 / e.getCouples().size();
				}
				if (edgePreferedLength < 20) {
					edgePreferedLength = 200;
				}
				double f = (edgePreferedLength - realLength) / (realLength * 3);

				// System.out.println("force is " + f);
				double dx = f * vx;
				double dy = f * vy;

				e.getFrom().setDx(e.getFrom().getDx() - dx);
				e.getFrom().setDy(e.getFrom().getDy() - dy);
				e.getTo().setDx(e.getTo().getDx() + dx);
				e.getTo().setDy(e.getTo().getDy() + dy);
			}
		}

		// the node repulsion
		Enumeration nodesEnum = model.getNodeList().elements();
		while (nodesEnum.hasMoreElements()) {
			Node n1 = (Node) nodesEnum.nextElement();
			if (!n1.isAutoLayoutProtected()) {

				double dx = 0;
				double dy = 0;

				if (n1.getCallers().size() < n1.getCallees().size()) {
					dy = 5 * n1.getCallees().size();
				} else if (n1.getCallers().size() > n1.getCallees().size()) {
					dy = -5 * n1.getCallers().size();
				}

				Enumeration nodesEnum2 = model.getNodeList().elements();
				while (nodesEnum2.hasMoreElements()) {
					Node n2 = (Node) nodesEnum2.nextElement();
					if (n1 != n2) {

						double vx = n1.getCenterX() - n2.getCenterX();
						double vy = n1.getCenterY() - n2.getCenterY();

						if (n1.getCenterX() - n2.getCenterX() > 0) {
							// n1 is on the right of n2
							if (n1.getCenterY() - n2.getCenterY() > 0) {
								// n1 is on the under n2
								if (n2.onDrawnObject(n1.getUpperLeftX(), n1
										.getUpperLeftY())) {
									vx = 100;
									// vy = 100;
									// System.out.println(n1.getLbl() + " is
									// overlapping " + n2.getLbl()+ "from bottom
									// Right");
								}
							} else {
								// n1 is on the upper n2
								if (n2.onDrawnObject(n1.getBottomLeftX(), n1
										.getBottomLeftY())) {
									vx = 100;
									// vy = -100;
									// System.out.println(n1.getLbl() + " is
									// overlapping " + n2.getLbl()+ "from top
									// Right");
								}
							}
						} else {
							// n1 is on the left of n2
							if (n1.getCenterY() - n2.getCenterY() > 0) {
								// n1 is on the under n2
								if (n2.onDrawnObject(n1.getUpperRightX(), n1
										.getUpperRightY())) {
									vx = -100;
									// vy = 100;
									// System.out.println(n1.getLbl() + " is
									// overlapping " + n2.getLbl()+ "from bottom
									// Left");
								}
							} else {
								// n1 is on the upper n2
								if (n2.onDrawnObject(n1.getBottomRightX(), n1
										.getBottomRightY())) {
									vx = -100;
									// vy = -100;
									// System.out.println(n1.getLbl() + " is
									// overlapping " + n2.getLbl()+ "from top
									// left");
								}
							}
						}

						double realLength = Math.sqrt(vx * vx + vy * vy);

						if (realLength == 0) {
							dx += Math.random();
							dy += Math.random();
						} else if (realLength < 1000) {
							dx += vx / realLength;
							dy += vy / realLength;
						}

					}
				}

				double dlen = Math.sqrt(dx * dx + dy * dy);

				if (dlen > 0) {
					dlen = Math.sqrt(dlen) / 2;
					n1.setDx(n1.getDx() + dx / dlen);
					n1.setDy(n1.getDy() + dy / dlen);
				}
			}
		}

		// move nodes and avoid graph limits
		Enumeration nodesEnum3 = model.getNodeList().elements();
		while (nodesEnum3.hasMoreElements()) {
			Node n = (Node) nodesEnum3.nextElement();
			if (!n.isAutoLayoutProtected()) {
				double x = n.getCenterX()
						+ Math.max(-5, Math.min(15, n.getDx()));
				double y = n.getCenterY()
						+ Math.max(-5, Math.min(15, n.getDy()));

				if (x < 0) {
					x = 20;
				} else if (x > model.getMaxPoint().x) {
					x += -20;
					;
				}

				if (y < 0) {
					y = 20;
				} else if (y > model.getMaxPoint().y) {
					y += -20;
				}
				n.setX(x);
				n.setY(y);
				n.setDx(n.getDx() / 2);
				n.setDy(n.getDy() / 2);
			}
		}

		model.fireModelChangedEvent();
	}
	/*
	 * spring algorithm
	 */
	static public void springRelaxGoodOldVersion(ArchitectureModel model) {
		// Edges impact
		Enumeration edgesEnum = model.getEdgesList().elements();
		while (edgesEnum.hasMoreElements()) {
			Edge e = (Edge) edgesEnum.nextElement();
			if (!e.isDeleted()) {
				double vx = e.getTo().getCenterX() - e.getFrom().getCenterX();
				double vy = e.getTo().getCenterY() - e.getFrom().getCenterY();
				double realLength = Math.sqrt(vx * vx + vy * vy);
				realLength = (realLength == 0) ? 0.0001 : realLength;

				int edgePreferedLength = 200;

				double f = (edgePreferedLength - realLength) / (realLength * 3);

				// System.out.println("force is " + f);
				double dx = f * vx;
				double dy = f * vy;

				e.getFrom().setDx(e.getFrom().getDx() - dx);
				e.getFrom().setDy(e.getFrom().getDy() - dy);
				e.getTo().setDx(e.getTo().getDx() + dx);
				e.getTo().setDy(e.getTo().getDy() + dy);
			}
		}

		// the node repulsion
		Enumeration nodesEnum = model.getNodeList().elements();
		while (nodesEnum.hasMoreElements()) {
			Node n1 = (Node) nodesEnum.nextElement();
			if (!n1.isAutoLayoutProtected()) {
				double dx = 0;
				double dy = 0;

				Enumeration nodesEnum2 = model.getNodeList().elements();
				while (nodesEnum2.hasMoreElements()) {
					Node n2 = (Node) nodesEnum2.nextElement();
					if (n1 != n2) {
						double vx = n1.getCenterX() - n2.getCenterX();
						double vy = n1.getCenterY() - n2.getCenterY();
						double realLength = Math.sqrt(vx * vx + vy * vy);

						if (realLength == 0) {
							dx += Math.random();
							dy += Math.random();
						} else if (realLength < 1000) {
							dx += vx / realLength;
							dy += vy / realLength;
						}
					}
				}

				double dlen = Math.sqrt(dx * dx + dy * dy);

				if (dlen > 0) {
					// dlen = Math.sqrt(dlen) / 2;
					n1.setDx(n1.getDx() + dx / dlen);
					n1.setDy(n1.getDy() + dy / dlen);
				}
			}
		}

		// move nodes and avoid graph limits
		Enumeration nodesEnum3 = model.getNodeList().elements();
		while (nodesEnum3.hasMoreElements()) {
			Node n = (Node) nodesEnum3.nextElement();
			if (!n.isAutoLayoutProtected()) {
				double x = n.getCenterX()
						+ Math.max(-5, Math.min(5, n.getDx()));
				double y = n.getCenterY()
						+ Math.max(-5, Math.min(5, n.getDy()));

				if (x < 0) {
					x = 20;
				} else if (x > model.getMaxPoint().x) {
					x += -20;
					;
				}

				if (y < 0) {
					y = 20;
				} else if (y > model.getMaxPoint().y) {
					y += -20;
				}
				n.setX(x);
				n.setY(y);
				n.setDx(n.getDx() / 2);
				n.setDy(n.getDy() / 2);
			}
		}
		model.fireModelChangedEvent();
	}
}
