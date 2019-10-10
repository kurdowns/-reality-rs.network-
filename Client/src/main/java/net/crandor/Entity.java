package net.crandor;

class Entity extends NodeSub {

	int highestY = 1000;

	public void renderAtPoint(int i, int j, int k, int l, int i1, int j1, int x, int y, long z) {
		Model model = getRotatedModel();
		if (model != null) {
			highestY = model.highestY;
			model.renderAtPoint(i, j, k, l, i1, j1, x, y, z);
		}

		model = getSpotAnimModel();
		if (model != null) {
			model.renderAtPoint(i, j, k, l, i1, j1, x, y, z);
		}
	}

	public Model getSpotAnimModel() {
		return null;
	}

	public Model getRotatedModel() {
		return null;
	}

}
