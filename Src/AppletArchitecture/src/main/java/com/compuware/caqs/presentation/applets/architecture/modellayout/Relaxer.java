package com.compuware.caqs.presentation.applets.architecture.modellayout;

import java.awt.geom.Point2D;
import java.util.Enumeration;

import com.compuware.caqs.domain.architecture.serializeddata.ArchitectureModel;
import com.compuware.caqs.domain.architecture.serializeddata.Edge;
import com.compuware.caqs.domain.architecture.serializeddata.Node;

public class Relaxer {
	boolean nodeOverlapping = true;

	/*
	 * Duplicate of the spring algorithm for test
	 */
	static public void springRelax() {
		gravity();
	}

	/*
	 * spring algorithm
	 */
	static public void gravity() {
		// Edges impact
		edgeImpact(1, ArchitectureModel.getInstance());
		// the node repulsion
		nodeRepulsion(4, ArchitectureModel.getInstance());

		// move nodes and avoid graph limits
		moveNodes(ArchitectureModel.getInstance());
		ArchitectureModel.getInstance().fireModelChangedEvent();
	}

	static private void edgeImpact(int factor, ArchitectureModel model) {
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

				double dx = f * vx;
				double dy = f * vy;

				if (!e.getFrom().isAutoLayoutProtected()) {
					e.getFrom().setDx(e.getFrom().getDx() - factor * dx);
					e.getFrom().setDy(e.getFrom().getDy() - factor * dy);
				}
				if (!e.getTo().isAutoLayoutProtected()) {
					e.getTo().setDx(e.getTo().getDx() + factor * dx);
					e.getTo().setDy(e.getTo().getDy() + factor * dy);
				}
			}
		}
	}

	static private void nodeRepulsion(int factor, ArchitectureModel model) {
		Enumeration nodesEnum = model.getNodeList().elements();
		while (nodesEnum.hasMoreElements()) {
			Node n1 = (Node) nodesEnum.nextElement();
			if (!n1.isDeleted() && !n1.isAutoLayoutProtected()) {

				double dx = 0;
				double dy = 0;

				// Node repulsion
				Enumeration nodesEnum2 = model.getNodeList().elements();
				while (nodesEnum2.hasMoreElements()) {
					Node n2 = (Node) nodesEnum2.nextElement();
					if (!n2.isDeleted()) {
						double vx = n1.getCenterX() - n2.getCenterX();
						double vy = n1.getCenterY() - n2.getCenterY();

						Point2D.Double p2 = avoidOverlap(n1, n2);
						vx += p2.x;
						vy += p2.y;

						if (!n1.getCallees().contains(n2)) {
							if (!n1.getCallers().contains(n2)) {
								if (n1 != n2) {
									double realLength = Math.sqrt(vx * vx + vy
											* vy);

									if (realLength == 0) {
										dx += Math.random();
										dy += Math.random();								
									} else if (realLength > 600) {
										dx += 0;
										dy += 0;
									} else {
										dx += vx / realLength;
										dy += vy / realLength;
									}
								}
							}
						}
					}
				}
				double dlen = Math.sqrt(dx * dx + dy * dy);

				if (dlen > 0) {
					dlen = Math.sqrt(dlen) / 2;

					n1.setDx(n1.getDx() + factor * dx / dlen);
					n1.setDy(n1.getDy() + factor * dy / dlen);
				}

			}
		}
	}

	static private Point2D.Double avoidOverlap(Node n1, Node n2) {
		double vx = n1.getCenterX() - n2.getCenterX();
		double vy = n1.getCenterY() - n2.getCenterY();

		if (n1.overlap(n2)) {
			switch (n1.relativePosition(n2)) {
			case Node.AL:
				vx -= 10;
				vy -= 10;
				// vx += n2.getUpperLeftX() - n1.getBottomRightX();
				// vy += n2.getUpperLeftY() - n1.getBottomRightY();
				break;
			case Node.AR:
				vx += 10;
				vy -= 10;
				// vx += n2.getUpperRightX() - n1.getBottomLeftX();
				// vy += n2.getUpperRightY() - n1.getBottomLeftY();
				break;
			case Node.BR:
				vx += 10;
				vy += 10;
				// vx += n2.getBottomRightX() - n1.getUpperLeftX();
				// vy += n2.getBottomRightY() - n1.getUpperLeftY();
				// System.out.println(n1.getLbl()+ " is be
				// under
				// " + n2.getLbl() );
				break;
			case Node.BL:
				vx -= 10;
				vy += 10;
				// vx += n2.getBottomLeftX() - n1.getUpperRightX();
				// vy += n2.getBottomLeftY() - n1.getUpperRightY();
				// System.out.println(n1.getLbl()+ " is be
				// under
				// " + n2.getLbl() );
				break;
			default:
				System.out.println("same level");
			}
		}
		return new Point2D.Double(vx, vy);
	}

	

	static private void moveNodes(ArchitectureModel model) {
		Enumeration nodesEnum3 = model.getNodeList().elements();
		while (nodesEnum3.hasMoreElements()) {
			Node n = (Node) nodesEnum3.nextElement();
			if (!n.isAutoLayoutProtected()) {
				// double x = n.getCenterX() + n.getDx();
				// double y = n.getCenterY() + n.getDy();
				double x = n.getCenterX()
						+ Math.max(-5, Math.min(5, n.getDx()));
				double y = n.getCenterY()
						+ Math.max(-5, Math.min(5, n.getDy()));

				n.setX(x);
				n.setY(y);

				if (n.getUpperLeftX() < 0) {
					if (n.getUpperLeftY() < 0) {
						translateAllModel(model, -n.getUpperLeftX(), -n
								.getUpperLeftY());
					} else {
						translateAllModel(model, -n.getUpperLeftX(), 0.0);
					}
				} else if (n.getUpperLeftY() < 0) {
					translateAllModel(model, 0.0, -n.getUpperLeftY());
				}

				n.setDx(n.getDx() / 2);
				n.setDy(n.getDy() / 2);
			}
		}
	}

	static private void translateAllModel(ArchitectureModel model, double dx,
			double dy) {
		Enumeration nodesEnum = model.getNodeList().elements();
		while (nodesEnum.hasMoreElements()) {
			Node n = (Node) nodesEnum.nextElement();
			if (!n.isAutoLayoutProtected()) {
				n.setX(n.getCenterX() + dx);
				n.setY(n.getCenterY() + dy);
			}
		}
	}
}
