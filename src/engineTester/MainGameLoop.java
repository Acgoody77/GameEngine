package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;

public class MainGameLoop {

	public static void main(String[] args) {

		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		//Raw Models
		ModelData treeData = OBJFileLoader.loadOBJ("lowPolyTree");
		RawModel treeModel = loader.loadToVAO(treeData.getVertices(), treeData.getTextureCoords(), treeData.getNormals(), treeData.getIndices());
		
		ModelData grassData = OBJFileLoader.loadOBJ("grassModel");
		RawModel grassModel = loader.loadToVAO(grassData.getVertices(), grassData.getTextureCoords(), grassData.getNormals(), grassData.getIndices());
		
		ModelData fernData = OBJFileLoader.loadOBJ("fern");
		RawModel fernModel = loader.loadToVAO(fernData.getVertices(), fernData.getTextureCoords(), fernData.getNormals(), fernData.getIndices());
		
		ModelData flowerData = OBJFileLoader.loadOBJ("fern");
		RawModel flowerModel = loader.loadToVAO(flowerData.getVertices(), flowerData.getTextureCoords(), flowerData.getNormals(), flowerData.getIndices());
		
		/*
		ModelData bunnyData = OBJFileLoader.loadOBJ("bunny");
		RawModel bunnyModel = loader.loadToVAO(bunnyData.getVertices(), bunnyData.getTextureCoords(), bunnyData.getNormals(), bunnyData.getIndices());
		*/
		ModelData personData = OBJFileLoader.loadOBJ("person");
		RawModel personModel = loader.loadToVAO(personData.getVertices(), personData.getTextureCoords(), personData.getNormals(), personData.getIndices());
		
		//Atlas textures
		ModelTexture fernTextureAtlas = new ModelTexture(loader.loadTexture("fern"));
		fernTextureAtlas.setNumberOfRows(2);
		
		//Textured Models
		TexturedModel tree = new TexturedModel(treeModel,new ModelTexture(loader.loadTexture("lowPolyTree")));
		
		TexturedModel grass = new TexturedModel(grassModel, new ModelTexture(loader.loadTexture("grassTexture")));
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		
		TexturedModel fern = new TexturedModel(fernModel , fernTextureAtlas);
		fern.getTexture().setHasTransparency(true);
		
		TexturedModel flower = new TexturedModel(flowerModel, new ModelTexture(loader.loadTexture("flower")));
		flower.getTexture().setHasTransparency(true);
		
		TexturedModel person = new TexturedModel(personModel, new ModelTexture(loader.loadTexture("playerTexture")));
		person.getTexture().setShineDamper(10);
		person.getTexture().setReflectivity(1);
		
		//Lights
		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(0,10000,-7000),new Vector3f(1,1,1)));
		lights.add(new Light(new Vector3f(-200,10,-200),new Vector3f(10,0,0)));
		lights.add(new Light(new Vector3f(200,10,200),new Vector3f(0,0,10)));
		//Terrains
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");
		Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap");
		
		//Entities
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		
		for(int i=0;i<400;i++){
			if(i % 20 == 0) {
				float x = random.nextFloat()*800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x,y,z),0,random.nextFloat() * 360,0,.5f));//atlas(use index)
			}
			if(i % 5 == 0) {
				float x = random.nextFloat()*800 - 400;
				float z = random.nextFloat() * -600;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(tree, new Vector3f(x,y,z),0,random.nextFloat() * 360,0,.5f));
				x = random.nextFloat()*800 - 400;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(grass, new Vector3f(x,y,z),0,random.nextFloat() * 360,0,1));
				x = random.nextFloat()*800 - 400;
				z = random.nextFloat() * -600;
				y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(flower, new Vector3f(x,y,z),0,random.nextFloat() * 360,0,.5f));
			}

		}
		
		//renderer
		MasterRenderer renderer = new MasterRenderer();
		
		//Player
		Player player = new Player(person, new Vector3f(100, 0, -50), 0, 0, 0, .5f);
		
		//Camera
		Camera camera = new Camera(player);	
		
		//GUI's
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture healthBar = new GuiTexture(loader.loadTexture("health"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		GuiTexture thinMatrix = new GuiTexture(loader.loadTexture("thinmatrix"), new Vector2f(0.3f, 0.59f), new Vector2f(0.4f, 0.4f));
		
		guis.add(thinMatrix);
		guis.add(healthBar);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		//Logic
		while(!Display.isCloseRequested()){
			player.move(terrain);
			camera.move();
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			for(Entity entity:entities){
				renderer.processEntity(entity);
			}
			renderer.render(lights, camera);
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
