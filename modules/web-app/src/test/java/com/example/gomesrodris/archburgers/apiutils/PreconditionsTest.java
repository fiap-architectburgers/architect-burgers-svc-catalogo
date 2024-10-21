package com.example.gomesrodris.archburgers.apiutils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PreconditionsTest {

	@Test
	void testCheckArgumentTrueExpression() {
		Preconditions.checkArgument(true, "Would throw if expression was not true");
		// Passed
	}

	@Test
	void testCheckArgumentFalseExpression() {
		IllegalArgumentException thrown = assertThrows(
				IllegalArgumentException.class,
				() -> Preconditions.checkArgument(false, "Expression is not true")
		);
		assertTrue(thrown.getMessage().contains("Expression is not true"));
	}
}