package mil.nga.sf.wkt.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;
import mil.nga.sf.CompoundCurve;
import mil.nga.sf.Curve;
import mil.nga.sf.Geometry;
import mil.nga.sf.GeometryCollection;
import mil.nga.sf.GeometryEnvelope;
import mil.nga.sf.GeometryType;
import mil.nga.sf.LineString;
import mil.nga.sf.MultiLineString;
import mil.nga.sf.MultiPoint;
import mil.nga.sf.MultiPolygon;
import mil.nga.sf.Point;
import mil.nga.sf.Polygon;
import mil.nga.sf.Surface;
import mil.nga.sf.extended.ExtendedGeometryCollection;
import mil.nga.sf.util.GeometryEnvelopeBuilder;
import mil.nga.sf.util.TextReader;
import mil.nga.sf.util.filter.FiniteFilterType;
import mil.nga.sf.util.filter.PointFiniteFilter;
import mil.nga.sf.wkt.GeometryReader;
import mil.nga.sf.wkt.GeometryWriter;

/**
 * Test Well Known Binary Geometries
 * 
 * @author osbornb
 */
public class WKTTest {

	/**
	 * Number of random geometries to create for each test
	 */
	private static final int GEOMETRIES_PER_TEST = 10;

	/**
	 * Constructor
	 */
	public WKTTest() {

	}

	/**
	 * Test a point
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testPoint() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a point
			Point point = WKTTestUtils.createPoint(WKTTestUtils.coinFlip(),
					WKTTestUtils.coinFlip());
			geometryTester(point);
		}

	}

	/**
	 * Test a line string
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testLineString() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a line string
			LineString lineString = WKTTestUtils.createLineString(
					WKTTestUtils.coinFlip(), WKTTestUtils.coinFlip());
			geometryTester(lineString);
		}

	}

	/**
	 * Test a polygon
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testPolygon() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a polygon
			Polygon polygon = WKTTestUtils.createPolygon(
					WKTTestUtils.coinFlip(), WKTTestUtils.coinFlip());
			geometryTester(polygon);
		}

	}

	/**
	 * Test a multi point
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testMultiPoint() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a multi point
			MultiPoint multiPoint = WKTTestUtils.createMultiPoint(
					WKTTestUtils.coinFlip(), WKTTestUtils.coinFlip());
			geometryTester(multiPoint);
		}

	}

	/**
	 * Test a multi line string
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testMultiLineString() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a multi line string
			MultiLineString multiLineString = WKTTestUtils
					.createMultiLineString(WKTTestUtils.coinFlip(),
							WKTTestUtils.coinFlip());
			geometryTester(multiLineString);
		}

	}

	/**
	 * Test a multi curve with line strings
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testMultiCurveWithLineStrings() throws IOException {

		// Test a pre-created WKT saved as the abstract MultiCurve type with
		// LineStrings

		String text = "MULTICURVE ( LINESTRING ( 18.889800697319032 -35.036463112927535, -37.76441919748682 -75.81115933696286, 68.9116399151478 -88.32707858422387 ), LINESTRING ( 145.52101409832818 -41.91160298025902, -173.4468670533211 11.756492650408305, -77.99433389977924 -39.554308892198534, 136.58380908612207 41.90364270668213, 39.97441553368359 -17.43335525530797, -121.31829755251131 -65.16951612235937, 49.88151008675286 7.029670331650452, 112.99918207874451 -35.62758965128506, -175.71124906933335 -36.04238233215776, -76.52909336488278 44.2390383216843 ) )";

		TestCase.assertTrue(text.startsWith(GeometryType.MULTICURVE.getName()));

		Geometry geometry = WKTTestUtils.readGeometry(text);
		TestCase.assertTrue(geometry instanceof GeometryCollection);
		TestCase.assertEquals(geometry.getGeometryType(),
				GeometryType.GEOMETRYCOLLECTION);
		@SuppressWarnings("unchecked")
		GeometryCollection<Curve> multiCurve = (GeometryCollection<Curve>) geometry;
		TestCase.assertEquals(2, multiCurve.numGeometries());
		Geometry geometry1 = multiCurve.getGeometries().get(0);
		Geometry geometry2 = multiCurve.getGeometries().get(1);
		TestCase.assertTrue(geometry1 instanceof LineString);
		TestCase.assertTrue(geometry2 instanceof LineString);
		LineString lineString1 = (LineString) geometry1;
		LineString lineString2 = (LineString) geometry2;
		TestCase.assertEquals(3, lineString1.numPoints());
		TestCase.assertEquals(10, lineString2.numPoints());
		Point point1 = lineString1.startPoint();
		Point point2 = lineString2.endPoint();
		TestCase.assertEquals(18.889800697319032, point1.getX());
		TestCase.assertEquals(-35.036463112927535, point1.getY());
		TestCase.assertEquals(-76.52909336488278, point2.getX());
		TestCase.assertEquals(44.2390383216843, point2.getY());

		ExtendedGeometryCollection<Curve> extendedMultiCurve = new ExtendedGeometryCollection<>(
				multiCurve);
		TestCase.assertEquals(GeometryType.MULTICURVE,
				extendedMultiCurve.getGeometryType());

		geometryTester(extendedMultiCurve, multiCurve);

		String text2 = WKTTestUtils.writeText(extendedMultiCurve);
		TestCase.assertTrue(
				text2.startsWith(GeometryType.MULTICURVE.getName()));
		WKTTestUtils.compareText(text, text2);

	}

	/**
	 * Test a multi curve with compound curve
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testMultiCurveWithCompoundCurve() throws IOException {

		// Test a pre-created WKT saved as the abstract MultiCurve type with a
		// CompoundCurve

		String text = "MULTICURVE (COMPOUNDCURVE (LINESTRING (3451418.006 5481808.951, 3451417.787 5481809.927, 3451409.995 5481806.744), LINESTRING (3451409.995 5481806.744, 3451418.006 5481808.951)))";

		TestCase.assertTrue(text.startsWith(GeometryType.MULTICURVE.getName()));

		Geometry geometry = WKTTestUtils.readGeometry(text);
		TestCase.assertTrue(geometry instanceof GeometryCollection);
		TestCase.assertEquals(geometry.getGeometryType(),
				GeometryType.GEOMETRYCOLLECTION);
		@SuppressWarnings("unchecked")
		GeometryCollection<Curve> multiCurve = (GeometryCollection<Curve>) geometry;
		TestCase.assertEquals(1, multiCurve.numGeometries());
		Geometry geometry1 = multiCurve.getGeometries().get(0);
		TestCase.assertTrue(geometry1 instanceof CompoundCurve);
		CompoundCurve compoundCurve1 = (CompoundCurve) geometry1;
		TestCase.assertEquals(2, compoundCurve1.numLineStrings());
		LineString lineString1 = compoundCurve1.getLineStrings().get(0);
		LineString lineString2 = compoundCurve1.getLineStrings().get(1);
		TestCase.assertEquals(3, lineString1.numPoints());
		TestCase.assertEquals(2, lineString2.numPoints());

		TestCase.assertEquals(new Point(3451418.006, 5481808.951),
				lineString1.getPoint(0));
		TestCase.assertEquals(new Point(3451417.787, 5481809.927),
				lineString1.getPoint(1));
		TestCase.assertEquals(new Point(3451409.995, 5481806.744),
				lineString1.getPoint(2));

		TestCase.assertEquals(new Point(3451409.995, 5481806.744),
				lineString2.getPoint(0));
		TestCase.assertEquals(new Point(3451418.006, 5481808.951),
				lineString2.getPoint(1));

		ExtendedGeometryCollection<Curve> extendedMultiCurve = new ExtendedGeometryCollection<>(
				multiCurve);
		TestCase.assertEquals(GeometryType.MULTICURVE,
				extendedMultiCurve.getGeometryType());

		geometryTester(extendedMultiCurve, multiCurve);

		String text2 = WKTTestUtils.writeText(extendedMultiCurve);
		TestCase.assertTrue(
				text2.startsWith(GeometryType.MULTICURVE.getName()));
		WKTTestUtils.compareText(text, text2);

	}

	/**
	 * Test a multi curve
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testMultiCurve() throws IOException {

		// Test the abstract MultiCurve type

		GeometryCollection<Curve> multiCurve = WKTTestUtils.createMultiCurve();

		String text = WKTTestUtils.writeText(multiCurve);

		ExtendedGeometryCollection<Curve> extendedMultiCurve = new ExtendedGeometryCollection<>(
				multiCurve);
		TestCase.assertEquals(GeometryType.MULTICURVE,
				extendedMultiCurve.getGeometryType());

		String extendedText = WKTTestUtils.writeText(extendedMultiCurve);

		TestCase.assertTrue(
				text.startsWith(GeometryType.GEOMETRYCOLLECTION.getName()));
		TestCase.assertTrue(
				extendedText.startsWith(GeometryType.MULTICURVE.getName()));

		Geometry geometry1 = WKTTestUtils.readGeometry(text);
		Geometry geometry2 = WKTTestUtils.readGeometry(extendedText);

		TestCase.assertTrue(geometry1 instanceof GeometryCollection);
		TestCase.assertTrue(geometry2 instanceof GeometryCollection);
		TestCase.assertEquals(GeometryType.GEOMETRYCOLLECTION,
				geometry1.getGeometryType());
		TestCase.assertEquals(GeometryType.GEOMETRYCOLLECTION,
				geometry2.getGeometryType());

		TestCase.assertEquals(multiCurve, geometry1);
		TestCase.assertEquals(geometry1, geometry2);

		@SuppressWarnings("unchecked")
		GeometryCollection<Geometry> geometryCollection1 = (GeometryCollection<Geometry>) geometry1;
		@SuppressWarnings("unchecked")
		GeometryCollection<Geometry> geometryCollection2 = (GeometryCollection<Geometry>) geometry2;
		TestCase.assertTrue(geometryCollection1.isMultiCurve());
		TestCase.assertTrue(geometryCollection2.isMultiCurve());

		geometryTester(multiCurve);
		geometryTester(extendedMultiCurve, multiCurve);
	}

	/**
	 * Test a multi surface
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testMultiSurface() throws IOException {

		// Test the abstract MultiSurface type

		GeometryCollection<Surface> multiSurface = WKTTestUtils
				.createMultiSurface();

		String text = WKTTestUtils.writeText(multiSurface);

		ExtendedGeometryCollection<Surface> extendedMultiSurface = new ExtendedGeometryCollection<>(
				multiSurface);
		TestCase.assertEquals(GeometryType.MULTISURFACE,
				extendedMultiSurface.getGeometryType());

		String extendedText = WKTTestUtils.writeText(extendedMultiSurface);

		TestCase.assertTrue(
				text.startsWith(GeometryType.GEOMETRYCOLLECTION.getName()));
		TestCase.assertTrue(
				extendedText.startsWith(GeometryType.MULTISURFACE.getName()));

		Geometry geometry1 = WKTTestUtils.readGeometry(text);
		Geometry geometry2 = WKTTestUtils.readGeometry(extendedText);

		TestCase.assertTrue(geometry1 instanceof GeometryCollection);
		TestCase.assertTrue(geometry2 instanceof GeometryCollection);
		TestCase.assertEquals(GeometryType.GEOMETRYCOLLECTION,
				geometry1.getGeometryType());
		TestCase.assertEquals(GeometryType.GEOMETRYCOLLECTION,
				geometry2.getGeometryType());

		TestCase.assertEquals(multiSurface, geometry1);
		TestCase.assertEquals(geometry1, geometry2);

		@SuppressWarnings("unchecked")
		GeometryCollection<Geometry> geometryCollection1 = (GeometryCollection<Geometry>) geometry1;
		@SuppressWarnings("unchecked")
		GeometryCollection<Geometry> geometryCollection2 = (GeometryCollection<Geometry>) geometry2;
		TestCase.assertTrue(geometryCollection1.isMultiSurface());
		TestCase.assertTrue(geometryCollection2.isMultiSurface());

		geometryTester(multiSurface);
		geometryTester(extendedMultiSurface, multiSurface);
	}

	/**
	 * Test a multi polygon
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testMultiPolygon() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a multi polygon
			MultiPolygon multiPolygon = WKTTestUtils.createMultiPolygon(
					WKTTestUtils.coinFlip(), WKTTestUtils.coinFlip());
			geometryTester(multiPolygon);
		}

	}

	/**
	 * Test a geometry collection
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testGeometryCollection() throws IOException {

		for (int i = 0; i < GEOMETRIES_PER_TEST; i++) {
			// Create and test a geometry collection
			GeometryCollection<Geometry> geometryCollection = WKTTestUtils
					.createGeometryCollection(WKTTestUtils.coinFlip(),
							WKTTestUtils.coinFlip());
			geometryTester(geometryCollection);
		}

	}

	/**
	 * Test a multi polygon 2.5
	 * 
	 * @throws IOException
	 *             upon error
	 */
	@Test
	public void testMultiPolygon25() throws IOException {

		// Test a pre-created WKT string saved as a 2.5D MultiPolygon

		String text = "MULTIPOLYGON Z(((-91.07087880858114 14.123634886445812 0.0,-91.07087285992856 14.123533759353165 0.0,-91.07105845788698 14.123550415580155 0.0,-91.07106797573107 14.12356112315473 0.0,-91.07112508279522 14.12359443560882 0.0,-91.07105144284623 14.123746753409705 0.0,-91.07104865928 14.123752510973361 0.0,-91.0709799356739 14.123874022276935 0.0,-91.07095614106379 14.123925180688502 0.0,-91.07092996699276 14.124102450533544 0.0,-91.07090855184373 14.124346345286652 0.0,-91.07090141346072 14.124415349655804 0.0,-91.07086453181506 14.12441891884731 0.0,-91.07087404965915 14.12390376553958 0.0,-91.07087880858114 14.123634886445812 0.0)))";

		TestCase.assertTrue(
				text.startsWith(GeometryType.MULTIPOLYGON.getName() + " Z"));

		Geometry geometry = WKTTestUtils.readGeometry(text);
		TestCase.assertTrue(geometry instanceof MultiPolygon);
		TestCase.assertEquals(geometry.getGeometryType(),
				GeometryType.MULTIPOLYGON);
		MultiPolygon multiPolygon = (MultiPolygon) geometry;
		TestCase.assertTrue(multiPolygon.hasZ());
		TestCase.assertFalse(multiPolygon.hasM());
		TestCase.assertEquals(1, multiPolygon.numGeometries());
		Polygon polygon = multiPolygon.getPolygon(0);
		TestCase.assertTrue(polygon.hasZ());
		TestCase.assertFalse(polygon.hasM());
		TestCase.assertEquals(1, polygon.numRings());
		LineString ring = polygon.getRing(0);
		TestCase.assertTrue(ring.hasZ());
		TestCase.assertFalse(ring.hasM());
		TestCase.assertEquals(15, ring.numPoints());
		for (Point point : ring.getPoints()) {
			TestCase.assertTrue(point.hasZ());
			TestCase.assertFalse(point.hasM());
			TestCase.assertNotNull(point.getZ());
			TestCase.assertNull(point.getM());
		}

		String multiPolygonText = WKTTestUtils.writeText(multiPolygon);
		WKTTestUtils.compareText(text, multiPolygonText);

		Geometry geometry2 = WKTTestUtils.readGeometry(multiPolygonText);

		geometryTester(geometry, geometry2);

	}

	/**
	 * Test geometry finite filtering
	 * 
	 * @throws Exception
	 *             upon error
	 */
	@Test
	public void testFiniteFilter() throws Exception {

		Point point = WKTTestUtils.createPoint(false, false);

		Point nan = new Point(Double.NaN, Double.NaN);
		Point nanZ = WKTTestUtils.createPoint(true, false);
		nanZ.setZ(Double.NaN);
		Point nanM = WKTTestUtils.createPoint(false, true);
		nanM.setM(Double.NaN);
		Point nanZM = WKTTestUtils.createPoint(true, true);
		nanZM.setZ(Double.NaN);
		nanZM.setM(Double.NaN);

		Point infinite = new Point(Double.POSITIVE_INFINITY,
				Double.POSITIVE_INFINITY);
		Point infiniteZ = WKTTestUtils.createPoint(true, false);
		infiniteZ.setZ(Double.POSITIVE_INFINITY);
		Point infiniteM = WKTTestUtils.createPoint(false, true);
		infiniteM.setM(Double.POSITIVE_INFINITY);
		Point infiniteZM = WKTTestUtils.createPoint(true, true);
		infiniteZM.setZ(Double.POSITIVE_INFINITY);
		infiniteZM.setM(Double.POSITIVE_INFINITY);

		Point nanInfinite = new Point(Double.NaN, Double.POSITIVE_INFINITY);
		Point nanInfiniteZM = WKTTestUtils.createPoint(true, true);
		nanInfiniteZM.setZ(Double.NaN);
		nanInfiniteZM.setM(Double.NEGATIVE_INFINITY);

		Point infiniteNan = new Point(Double.POSITIVE_INFINITY, Double.NaN);
		Point infiniteNanZM = WKTTestUtils.createPoint(true, true);
		infiniteNanZM.setZ(Double.NEGATIVE_INFINITY);
		infiniteNanZM.setM(Double.NaN);

		LineString lineString1 = new LineString();
		lineString1.addPoint(point);
		lineString1.addPoint(nan);
		lineString1.addPoint(WKTTestUtils.createPoint(false, false));
		lineString1.addPoint(infinite);
		lineString1.addPoint(WKTTestUtils.createPoint(false, false));
		lineString1.addPoint(nanInfinite);
		lineString1.addPoint(WKTTestUtils.createPoint(false, false));
		lineString1.addPoint(infiniteNan);

		LineString lineString2 = new LineString(true, false);
		lineString2.addPoint(WKTTestUtils.createPoint(true, false));
		lineString2.addPoint(nanZ);
		lineString2.addPoint(WKTTestUtils.createPoint(true, false));
		lineString2.addPoint(infiniteZ);

		LineString lineString3 = new LineString(false, true);
		lineString3.addPoint(WKTTestUtils.createPoint(false, true));
		lineString3.addPoint(nanM);
		lineString3.addPoint(WKTTestUtils.createPoint(false, true));
		lineString3.addPoint(infiniteM);

		LineString lineString4 = new LineString(true, true);
		lineString4.addPoint(WKTTestUtils.createPoint(true, true));
		lineString4.addPoint(nanZM);
		lineString4.addPoint(WKTTestUtils.createPoint(true, true));
		lineString4.addPoint(infiniteZM);
		lineString4.addPoint(WKTTestUtils.createPoint(true, true));
		lineString4.addPoint(nanInfiniteZM);
		lineString4.addPoint(WKTTestUtils.createPoint(true, true));
		lineString4.addPoint(infiniteNanZM);

		Polygon polygon1 = new Polygon(lineString1);
		Polygon polygon2 = new Polygon(lineString2);
		Polygon polygon3 = new Polygon(lineString3);
		Polygon polygon4 = new Polygon(lineString4);

		for (Point pnt : lineString1.getPoints()) {
			testFiniteFilter(pnt);
		}

		for (Point pnt : lineString2.getPoints()) {
			testFiniteFilter(pnt);
		}

		for (Point pnt : lineString3.getPoints()) {
			testFiniteFilter(pnt);
		}

		for (Point pnt : lineString4.getPoints()) {
			testFiniteFilter(pnt);
		}

		testFiniteFilter(lineString1);
		testFiniteFilter(lineString2);
		testFiniteFilter(lineString3);
		testFiniteFilter(lineString4);
		testFiniteFilter(polygon1);
		testFiniteFilter(polygon2);
		testFiniteFilter(polygon3);
		testFiniteFilter(polygon4);

	}

	/**
	 * Test the geometry writing to and reading from text
	 * 
	 * @param geometry
	 *            geometry
	 * @throws IOException
	 */
	private void geometryTester(Geometry geometry) throws IOException {

		geometryTester(geometry, geometry);

	}

	/**
	 * Test the geometry writing to and reading from text, compare with the
	 * provided geometry
	 * 
	 * @param geometry
	 *            geometry
	 * @param compareGeometry
	 *            compare geometry
	 * @throws IOException
	 */
	private void geometryTester(Geometry geometry, Geometry compareGeometry)
			throws IOException {

		// Write the geometry to text
		String text = WKTTestUtils.writeText(geometry);

		// Test the geometry read from text
		Geometry geometryFromText = WKTTestUtils.readGeometry(text);
		WKTTestUtils.compareText(WKTTestUtils.writeText(compareGeometry),
				WKTTestUtils.writeText(geometryFromText));

		WKTTestUtils.compareGeometries(compareGeometry, geometryFromText);

		GeometryEnvelope envelope = GeometryEnvelopeBuilder
				.buildEnvelope(compareGeometry);
		GeometryEnvelope envelopeFromText = GeometryEnvelopeBuilder
				.buildEnvelope(geometryFromText);

		WKTTestUtils.compareEnvelopes(envelope, envelopeFromText);
	}

	/**
	 * Test fine filter for the geometry
	 * 
	 * @param geometry
	 *            geometry
	 * @throws Exception
	 */
	private void testFiniteFilter(Geometry geometry) throws Exception {

		String text = GeometryWriter.writeGeometry(geometry);

		testFiniteFilter(text, new PointFiniteFilter());
		testFiniteFilter(text, new PointFiniteFilter(true));
		testFiniteFilter(text, new PointFiniteFilter(false, true));
		testFiniteFilter(text, new PointFiniteFilter(true, true));
		testFiniteFilter(text,
				new PointFiniteFilter(FiniteFilterType.FINITE_AND_NAN));
		testFiniteFilter(text,
				new PointFiniteFilter(FiniteFilterType.FINITE_AND_NAN, true));
		testFiniteFilter(text, new PointFiniteFilter(
				FiniteFilterType.FINITE_AND_NAN, false, true));
		testFiniteFilter(text, new PointFiniteFilter(
				FiniteFilterType.FINITE_AND_NAN, true, true));
		testFiniteFilter(text,
				new PointFiniteFilter(FiniteFilterType.FINITE_AND_INFINITE));
		testFiniteFilter(text, new PointFiniteFilter(
				FiniteFilterType.FINITE_AND_INFINITE, true));
		testFiniteFilter(text, new PointFiniteFilter(
				FiniteFilterType.FINITE_AND_INFINITE, false, true));
		testFiniteFilter(text, new PointFiniteFilter(
				FiniteFilterType.FINITE_AND_INFINITE, true, true));

	}

	/**
	 * Filter and validate the geometry text
	 * 
	 * @param text
	 *            geometry text
	 * @param filter
	 *            point finite filter
	 * @throws IOException
	 *             upon error
	 */
	private void testFiniteFilter(String text, PointFiniteFilter filter)
			throws IOException {

		TextReader reader = new TextReader(text);
		Geometry geometry = GeometryReader.readGeometry(reader, filter);
		reader.close();

		if (geometry != null) {

			List<Point> points = new ArrayList<>();

			switch (geometry.getGeometryType()) {
			case POINT:
				points.add((Point) geometry);
				break;
			case LINESTRING:
				points.addAll(((LineString) geometry).getPoints());
				break;
			case POLYGON:
				points.addAll(((Polygon) geometry).getRing(0).getPoints());
				break;
			default:
				TestCase.fail(
						"Unexpected test case: " + geometry.getGeometryType());
			}

			for (Point point : points) {

				switch (filter.getType()) {
				case FINITE:
					TestCase.assertTrue(Double.isFinite(point.getX()));
					TestCase.assertTrue(Double.isFinite(point.getY()));
					if (filter.isFilterZ() && point.hasZ()) {
						TestCase.assertTrue(Double.isFinite(point.getZ()));
					}
					if (filter.isFilterM() && point.hasM()) {
						TestCase.assertTrue(Double.isFinite(point.getM()));
					}
					break;
				case FINITE_AND_NAN:
					TestCase.assertTrue(Double.isFinite(point.getX())
							|| Double.isNaN(point.getX()));
					TestCase.assertTrue(Double.isFinite(point.getY())
							|| Double.isNaN(point.getY()));
					if (filter.isFilterZ() && point.hasZ()) {
						TestCase.assertTrue(Double.isFinite(point.getZ())
								|| Double.isNaN(point.getZ()));
					}
					if (filter.isFilterM() && point.hasM()) {
						TestCase.assertTrue(Double.isFinite(point.getM())
								|| Double.isNaN(point.getM()));
					}
					break;
				case FINITE_AND_INFINITE:
					TestCase.assertTrue(Double.isFinite(point.getX())
							|| Double.isInfinite(point.getX()));
					TestCase.assertTrue(Double.isFinite(point.getY())
							|| Double.isInfinite(point.getY()));
					if (filter.isFilterZ() && point.hasZ()) {
						TestCase.assertTrue(Double.isFinite(point.getZ())
								|| Double.isInfinite(point.getZ()));
					}
					if (filter.isFilterM() && point.hasM()) {
						TestCase.assertTrue(Double.isFinite(point.getM())
								|| Double.isInfinite(point.getM()));
					}
					break;
				}

			}
		}

	}

}
