package it.polimi.se2019.limperio.nicotera.italia.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestMapSecondType {

    Map map;
    @Before
    public void creationOfMap() {
        map = Map.instanceOfMap(2);
    }
    @After
    public void deleteMap(){
        map.setInstanceOfMapForTesting();
    }
    @Test
    public void testMapType2(){
        Assert.assertEquals(map.getMatrixOfSquares()[0][0].getColor(), ColorOfFigure_Square.BLUE);
        Assert.assertTrue(map.getMatrixOfSquares()[0][0].isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[0][0].isSpawn());
        Assert.assertEquals(map.getMatrixOfSquares()[0][1].getColor(), ColorOfFigure_Square.BLUE);
        Assert.assertFalse(map.getMatrixOfSquares()[0][1].isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[0][1].isSpawn());
        Assert.assertEquals(map.getMatrixOfSquares()[0][2].getColor(), ColorOfFigure_Square.BLUE);
        Assert.assertTrue(map.getMatrixOfSquares()[0][2].isHasDoor());
        Assert.assertTrue(map.getMatrixOfSquares()[0][2].isSpawn());
        Assert.assertEquals(map.getMatrixOfSquares()[0][3].getColor(), ColorOfFigure_Square.GREEN);
        Assert.assertTrue(map.getMatrixOfSquares()[0][3].isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[0][3].isSpawn());
        Assert.assertEquals(map.getMatrixOfSquares()[1][0].getColor(), ColorOfFigure_Square.RED);
        Assert.assertTrue(map.getMatrixOfSquares()[1][0].isHasDoor());
        Assert.assertTrue(map.getMatrixOfSquares()[1][0].isSpawn());
        Assert.assertEquals(map.getMatrixOfSquares()[1][1].getColor(), ColorOfFigure_Square.RED);
        Assert.assertTrue(map.getMatrixOfSquares()[1][1].isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[1][1].isSpawn());
        Assert.assertEquals(map.getMatrixOfSquares()[1][2].getColor(), ColorOfFigure_Square.YELLOW);
        Assert.assertTrue(map.getMatrixOfSquares()[1][2].isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[1][2].isSpawn());
        Assert.assertEquals(map.getMatrixOfSquares()[1][3].getColor(), ColorOfFigure_Square.YELLOW);
        Assert.assertTrue(map.getMatrixOfSquares()[1][3].isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[1][3].isSpawn());
        Assert.assertNull(map.getMatrixOfSquares()[2][0]);
        Assert.assertEquals(map.getMatrixOfSquares()[2][1].getColor(), ColorOfFigure_Square.GREY);
        Assert.assertTrue(map.getMatrixOfSquares()[2][1].isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[2][1].isSpawn());
        Assert.assertEquals(map.getMatrixOfSquares()[2][2].getColor(), ColorOfFigure_Square.YELLOW);
        Assert.assertTrue(map.getMatrixOfSquares()[2][2].isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[2][2].isSpawn());
        Assert.assertEquals(map.getMatrixOfSquares()[2][3].getColor(), ColorOfFigure_Square.YELLOW);
        Assert.assertFalse(map.getMatrixOfSquares()[2][3].isHasDoor());
        Assert.assertTrue(map.getMatrixOfSquares()[2][3].isSpawn());
        testSetAdjForSecondType();
    }

    private void testSetAdjForSecondType(){
        map.setAdjForSecondType();
        assertNull(map.getMatrixOfSquares()[0][0].getNorth());
        assertEquals(map.getMatrixOfSquares()[0][0].getSouth().getColor(), ColorOfFigure_Square.RED);
        Assert.assertTrue(map.getMatrixOfSquares()[0][0].getSouth().isHasDoor());
        Assert.assertTrue(map.getMatrixOfSquares()[0][0].getSouth().isSpawn());
        assertNull(map.getMatrixOfSquares()[0][0].getWest());
        assertEquals(map.getMatrixOfSquares()[0][0].getEast().getColor(), ColorOfFigure_Square.BLUE);
        Assert.assertFalse(map.getMatrixOfSquares()[0][0].getEast().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[0][0].getEast().isSpawn());
        assertNull(map.getMatrixOfSquares()[0][1].getNorth());
        assertNull(map.getMatrixOfSquares()[0][1].getSouth());
        assertEquals(map.getMatrixOfSquares()[0][1].getWest().getColor(), ColorOfFigure_Square.BLUE);
        Assert.assertTrue(map.getMatrixOfSquares()[0][1].getWest().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[0][1].getWest().isSpawn());
        assertEquals(map.getMatrixOfSquares()[0][1].getEast().getColor(), ColorOfFigure_Square.BLUE);
        Assert.assertTrue(map.getMatrixOfSquares()[0][1].getEast().isHasDoor());
        Assert.assertTrue(map.getMatrixOfSquares()[0][1].getEast().isSpawn());
        assertNull(map.getMatrixOfSquares()[0][2].getNorth());
        assertEquals(map.getMatrixOfSquares()[0][2].getSouth().getColor(), ColorOfFigure_Square.YELLOW);
        Assert.assertTrue(map.getMatrixOfSquares()[0][2].getSouth().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[0][2].getSouth().isSpawn());
        assertEquals(map.getMatrixOfSquares()[0][2].getWest().getColor(), ColorOfFigure_Square.BLUE);
        Assert.assertFalse(map.getMatrixOfSquares()[0][2].getWest().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[0][2].getWest().isSpawn());
        assertEquals(map.getMatrixOfSquares()[0][2].getEast().getColor(), ColorOfFigure_Square.GREEN);
        Assert.assertTrue(map.getMatrixOfSquares()[0][2].getEast().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[0][2].getEast().isSpawn());
        assertNull(map.getMatrixOfSquares()[0][3].getNorth());
        assertEquals(map.getMatrixOfSquares()[0][3].getSouth().getColor(), ColorOfFigure_Square.YELLOW);
        Assert.assertTrue(map.getMatrixOfSquares()[0][3].getSouth().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[0][3].getSouth().isSpawn());
        assertEquals(map.getMatrixOfSquares()[0][3].getWest().getColor(), ColorOfFigure_Square.BLUE);
        Assert.assertTrue(map.getMatrixOfSquares()[0][3].getWest().isHasDoor());
        Assert.assertTrue(map.getMatrixOfSquares()[0][3].getWest().isSpawn());
        assertNull(map.getMatrixOfSquares()[0][3].getEast());
        assertEquals(map.getMatrixOfSquares()[1][0].getNorth().getColor(), ColorOfFigure_Square.BLUE);
        Assert.assertTrue(map.getMatrixOfSquares()[1][0].getNorth().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[1][0].getNorth().isSpawn());
        assertNull(map.getMatrixOfSquares()[1][0].getSouth());
        assertNull(map.getMatrixOfSquares()[1][0].getWest());
        assertEquals(map.getMatrixOfSquares()[1][0].getEast().getColor(), ColorOfFigure_Square.RED);
        Assert.assertTrue(map.getMatrixOfSquares()[1][0].getEast().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[1][0].getEast().isSpawn());
        assertNull(map.getMatrixOfSquares()[1][1].getNorth());
        assertEquals(map.getMatrixOfSquares()[1][1].getSouth().getColor(), ColorOfFigure_Square.GREY);
        Assert.assertTrue(map.getMatrixOfSquares()[1][1].getSouth().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[1][1].getSouth().isSpawn());
        assertEquals(map.getMatrixOfSquares()[1][1].getWest().getColor(), ColorOfFigure_Square.RED);
        Assert.assertTrue(map.getMatrixOfSquares()[1][1].getWest().isHasDoor());
        Assert.assertTrue(map.getMatrixOfSquares()[1][1].getWest().isSpawn());
        assertEquals(map.getMatrixOfSquares()[1][1].getWest().getColor(), ColorOfFigure_Square.RED);
        Assert.assertTrue(map.getMatrixOfSquares()[1][1].getWest().isHasDoor());
        Assert.assertTrue(map.getMatrixOfSquares()[1][1].getWest().isSpawn());
        assertEquals(map.getMatrixOfSquares()[1][2].getNorth().getColor(), ColorOfFigure_Square.BLUE);
        Assert.assertTrue(map.getMatrixOfSquares()[1][2].getNorth().isHasDoor());
        Assert.assertTrue(map.getMatrixOfSquares()[1][2].getNorth().isSpawn());
        assertEquals(map.getMatrixOfSquares()[1][2].getSouth().getColor(), ColorOfFigure_Square.YELLOW);
        Assert.assertTrue(map.getMatrixOfSquares()[1][2].getSouth().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[1][2].getSouth().isSpawn());
        assertNull(map.getMatrixOfSquares()[1][2].getWest());
        assertEquals(map.getMatrixOfSquares()[1][2].getEast().getColor(), ColorOfFigure_Square.YELLOW);
        Assert.assertTrue(map.getMatrixOfSquares()[1][2].getEast().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[1][2].getEast().isSpawn());
        assertEquals(map.getMatrixOfSquares()[1][3].getNorth().getColor(), ColorOfFigure_Square.GREEN);
        Assert.assertTrue(map.getMatrixOfSquares()[1][3].getNorth().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[1][3].getNorth().isSpawn());
        assertEquals(map.getMatrixOfSquares()[1][3].getSouth().getColor(), ColorOfFigure_Square.YELLOW);
        Assert.assertFalse(map.getMatrixOfSquares()[1][3].getSouth().isHasDoor());
        Assert.assertTrue(map.getMatrixOfSquares()[1][3].getSouth().isSpawn());
        assertEquals(map.getMatrixOfSquares()[1][3].getWest().getColor(), ColorOfFigure_Square.YELLOW);
        Assert.assertTrue(map.getMatrixOfSquares()[1][3].getWest().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[1][3].getWest().isSpawn());
        assertNull(map.getMatrixOfSquares()[1][3].getEast());
        assertEquals(map.getMatrixOfSquares()[2][1].getNorth().getColor(), ColorOfFigure_Square.RED);
        assertNull(map.getMatrixOfSquares()[2][1].getSouth());
        assertNull(map.getMatrixOfSquares()[2][1].getWest());
        assertEquals(map.getMatrixOfSquares()[2][1].getEast().getColor(), ColorOfFigure_Square.YELLOW);
        Assert.assertTrue(map.getMatrixOfSquares()[2][1].getEast().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[2][1].getEast().isSpawn());
        assertEquals(map.getMatrixOfSquares()[2][2].getNorth().getColor(), ColorOfFigure_Square.YELLOW);
        Assert.assertTrue(map.getMatrixOfSquares()[2][2].getNorth().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[2][2].getNorth().isSpawn());
        assertNull(map.getMatrixOfSquares()[2][2].getSouth());
        assertEquals(map.getMatrixOfSquares()[2][2].getWest().getColor(), ColorOfFigure_Square.GREY);
        Assert.assertTrue(map.getMatrixOfSquares()[2][2].getWest().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[2][2].getWest().isSpawn());
        assertEquals(map.getMatrixOfSquares()[2][2].getEast().getColor(), ColorOfFigure_Square.YELLOW);
        Assert.assertFalse(map.getMatrixOfSquares()[2][2].getEast().isHasDoor());
        Assert.assertTrue(map.getMatrixOfSquares()[2][2].getEast().isSpawn());
        assertEquals(map.getMatrixOfSquares()[2][3].getNorth().getColor(), ColorOfFigure_Square.YELLOW);
        Assert.assertTrue(map.getMatrixOfSquares()[2][3].getNorth().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[2][3].getNorth().isSpawn());
        assertNull(map.getMatrixOfSquares()[2][3].getSouth());
        assertEquals(map.getMatrixOfSquares()[2][3].getWest().getColor(), ColorOfFigure_Square.YELLOW);
        Assert.assertTrue(map.getMatrixOfSquares()[2][3].getWest().isHasDoor());
        Assert.assertFalse(map.getMatrixOfSquares()[2][3].getWest().isSpawn());
        assertNull(map.getMatrixOfSquares()[2][3].getEast());
    }
}
