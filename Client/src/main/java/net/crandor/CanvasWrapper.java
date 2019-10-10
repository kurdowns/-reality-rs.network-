package net.crandor;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Graphics;

public class CanvasWrapper extends Canvas {
	private Component component;

	@Override
	public void paint(Graphics graphics) {
		component.paint(graphics);
	}

	@Override
	public void update(Graphics graphics) {
		component.update(graphics);
	}

	CanvasWrapper(Component c) {
		component = c;
	}
}
