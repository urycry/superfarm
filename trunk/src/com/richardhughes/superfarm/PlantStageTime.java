package com.richardhughes.superfarm;

public enum PlantStageTime {

	// the plant is seed stage
	Seed,

	// before the plant has flowered, but after it has seeded
	Seedling,
	Flower,
	Fruit,
	Dead,

	// no more, should not be rendered and should be removed from game
	Gone,
}
