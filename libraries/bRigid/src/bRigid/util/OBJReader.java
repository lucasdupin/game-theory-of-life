package bRigid.util;

import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

/**
 * This class is not part of the Processing API and should not be used directly.
 * Instead, use loadShape() and methods like it, which will make use of this
 * class. Using this class directly will cause your code to break when combined
 * with future versions of Processing.
 * <p>
 * OBJ loading implemented using code from Saito's OBJLoader library:
 * http://code.google.com/p/saitoobjloader/ and OBJReader from Ahmet Kizilay
 * http://www.openprocessing.org/visuals/?visualID=191
 * 
 */
public class OBJReader {

	public ArrayList<PShape> shapes;

	public ArrayList<OBJFace> faces;

	public ArrayList<PVector> coords;
	
	public ArrayList<OBJGroup> groups;

	/**
	 * Initializes a new OBJ Object with the given filename.
	 */
	public OBJReader(PApplet parent, String filename) {
		this(parent, parent.createReader(filename), getBasePath(parent, filename));
	}

	public OBJReader(PApplet parent, BufferedReader reader) {
		this(parent, reader, "");
	}

	public OBJReader(PApplet parent, BufferedReader reader, String basePath) {
		shapes = new ArrayList<PShape>();
		groups = new ArrayList<OBJGroup>();
		faces = new ArrayList<OBJFace>();
		coords = new ArrayList<PVector>();
		ArrayList<PVector> normals = new ArrayList<PVector>();
		parseOBJ(parent, basePath, reader, groups, faces, coords, normals);

		addChildren(parent, groups, faces, coords, normals);
	}

	protected void addChildren(PApplet p, ArrayList<OBJGroup> groups, ArrayList<OBJFace> faces, ArrayList<PVector> coords,
			ArrayList<PVector> normals) {
		for (int j = 0; j < groups.size(); j++) {

			OBJGroup objGroup = groups.get(j);
			processing.core.PShape group;
			group = p.createShape(0);

			for (int i = 0; i < objGroup.faceIdx.size(); i++) {
				OBJFace face = objGroup.faceIdx.get(i);
				// Creating child shape for current face.
				// PShape child = new BShapeOBJ(face, coords, normals);
				PShape child = addFace(p, face, coords, normals);
				group.addChild(child);
			}
			shapes.add(group);
		}
	}

	protected PShape addFace(PApplet p, OBJFace face, ArrayList<PVector> coords, ArrayList<PVector> normals) {

		PShape mesh;
		mesh = p.createShape();
		mesh.beginShape();

		for (int i = 0; i < face.normIdx.size(); i++) {
			int ID = face.normIdx.get(i);
			PVector n = normals.get(ID - 1);
			mesh.normal(n.x, n.y, n.z);
		}

		for (int i = 0; i < face.vertIdx.size(); i++) {
			int ID = face.vertIdx.get(i);
			PVector v = coords.get(ID - 1);
			mesh.vertex(v.x, -v.y, v.z);
		}
		mesh.endShape();
		return mesh;
	}

	

	static protected void parseOBJ(PApplet parent, String path, BufferedReader reader, ArrayList<OBJGroup> groups,
			ArrayList<OBJFace> faces, ArrayList<PVector> coords, ArrayList<PVector> normals) {
		boolean readv, readvn;
		try {

			readv = readvn = false;
			String line;
			while ((line = reader.readLine()) != null) {
				// Parse the line.
				line = line.trim();
				if (line.equals("") || line.indexOf('#') == 0) {
					// Empty line of comment, ignore line
					continue;
				}

				// The below patch/hack comes from Carlos Tomas Marti and is a
				// fix for single backslashes in Rhino obj files

				// BEGINNING OF RHINO OBJ FILES HACK
				// Statements can be broken in multiple lines using '\' at the
				// end of a line.
				// In regular expressions, the backslash is also an escape
				// character.
				// The regular expression \\ matches a single backslash. This
				// regular expression as a Java string, becomes "\\\\".
				// That's right: 4 backslashes to match a single one.
				while (line.contains("\\")) {
					line = line.split("\\\\")[0];
					final String s = reader.readLine();
					if (s != null)
						line += s;
				}
				// END OF RHINO OBJ FILES HACK

				String[] parts = line.split("\\s+");
				// if not a blank line, process the line.
				if (parts.length > 0) {
					if (parts[0].equals("v")) {
						// vertex
						PVector tempv = new PVector(Float.valueOf(parts[1]).floatValue(), Float.valueOf(parts[2]).floatValue(), Float
								.valueOf(parts[3]).floatValue());
						coords.add(tempv);
						readv = true;
						//OBJGroup g = groups.get(groups.size() - 1);
						//g.groupCoords.add(tempv);
					} else if (parts[0].equals("vn")) {
						// normal
						PVector tempn = new PVector(Float.valueOf(parts[1]).floatValue(), Float.valueOf(parts[2]).floatValue(), Float
								.valueOf(parts[3]).floatValue());
						normals.add(tempn);
						readvn = true;
					} else if (parts[0].equals("g")) {
						// gname = 1 < parts.length ? parts[1] : "";
						OBJGroup group = new OBJGroup();
						groups.add(group);

					} else if (parts[0].equals("f")) {
						// Face setting
						OBJFace face = new OBJFace();
						// face.name = gname;

						for (int i = 1; i < parts.length; i++) {
							String seg = parts[i];

							if (seg.indexOf("/") > 0) {
								String[] forder = seg.split("/");

								if (forder.length > 2) {
									// Getting vertex and texture and normal
									// indexes.
									if (forder[0].length() > 0 && readv) {
										face.vertIdx.add(Integer.valueOf(forder[0]));
									}
									if (forder[2].length() > 0 && readvn) {
										face.normIdx.add(Integer.valueOf(forder[2]));
									}
								} else if (forder.length > 1) {
									// Getting vertex and texture/normal
									// indexes.
									if (forder[0].length() > 0 && readv) {
										face.vertIdx.add(Integer.valueOf(forder[0]));
									}
									if (forder[1].length() > 0) {
										if (readvn) {
											face.normIdx.add(Integer.valueOf(forder[1]));
										}
									}
								} else if (forder.length > 0) {
									// Getting vertex index only.
									if (forder[0].length() > 0 && readv) {
										face.vertIdx.add(Integer.valueOf(forder[0]));
									}
								}
							} else {
								// Getting vertex index only.
								if (seg.length() > 0 && readv) {
									face.vertIdx.add(Integer.valueOf(seg));
								}
							}
						}
						faces.add(face);
						OBJGroup g = groups.get(groups.size() - 1);
						g.faceIdx.add(face);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	// Stores a face from an OBJ file
	static public class OBJFace {
		public ArrayList<Integer> vertIdx;
		ArrayList<Integer> normIdx;

		OBJFace() {
			vertIdx = new ArrayList<Integer>();
			normIdx = new ArrayList<Integer>();
		}
	}

	// Stores a group from an OBJ file
	static public class OBJGroup {
		public ArrayList<OBJFace> faceIdx;
		public ArrayList<PVector> groupCoords;

		OBJGroup() {
			faceIdx = new ArrayList<OBJFace>();
			groupCoords = new ArrayList<PVector>();
		}
	}

	static protected String getBasePath(PApplet parent, String filename) {
		// Obtaining the path
		File file = new File(parent.dataPath(filename));
		if (!file.exists()) {
			file = parent.sketchFile(filename);
		}
		String absolutePath = file.getAbsolutePath();
		return absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
	}

}