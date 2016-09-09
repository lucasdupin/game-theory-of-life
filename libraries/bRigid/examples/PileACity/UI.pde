ArrayList<BObject> importAllBodies(String folderName) {
  final File folder = new File(folderName);
  ArrayList<String> fileNames = new ArrayList<String>();
  for (final File fileEntry : folder.listFiles()) {
    if (!fileEntry.isDirectory()) {
      //println(fileEntry.getName());
      if (fileEntry.getName().endsWith(".obj")) fileNames.add(folderName+ "/"+fileEntry.getName());
    }
  }

  ArrayList<BObject> bodies = new ArrayList<BObject>();

  for (int i=0;i<fileNames.size();i++) {
    String fileName =  (String) fileNames.get(i);
    println(fileName);
    BGImpactMesh mesh = new BGImpactMesh(this, 1, fileName, new Vector3f(), true);
    mesh.displayShape = loadShape(fileName);
    mesh.displayShape.setFill(color(random(200),0,0));
    bodies.add(mesh );
  }
  return bodies;
}