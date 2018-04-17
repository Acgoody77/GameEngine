package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import models.RawModel;
import models.TexturedModel;
import objConverter.ModelData;
import objConverter.OBJFileLoader;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;
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
		
		ModelData lampData = OBJFileLoader.loadOBJ("lamp");
		RawModel lampModel = loader.loadToVAO(lampData.getVertices(), lampData.getTextureCoords(), lampData.getNormals(), lampData.getIndices());
		
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
		
		TexturedModel lamp = new TexturedModel(lampModel, new ModelTexture(loader.loadTexture("lamp")));
		
		
		//Lights
		List<Light> lights = new ArrayList<Light>();
		Light sun = (new Light(new Vector3f(0,1000,-500),new Vector3f(0.7f,0.7f,0.7f)));
		lights.add(sun);//sun
		lights.add(new Light(new Vector3f(185,10,-293),new Vector3f(2,0,0), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(new Light(new Vector3f(200,17,-200),new Vector3f(0,2,2), new Vector3f(1, 0.01f, 0.002f)));
		Light lampLight = (new Light(new Vector3f(293, 7, -305), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(lampLight);
		
		
		//Terrains
		List<Terrain> terrains = new ArrayList<Terrain>();
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("smallIslandBlendMap"));
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "smallIsland");
		//Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap, "heightmap");
		terrains.add(terrain);
		
		
		//Entities
		List<Entity> entities = new ArrayList<Entity>();
		Random random = new Random();
		
		/*
		 * 		  z
		 * 	   x	-x
		 *		 -z
		 */
		for(int i=0;i<400;i++){
			if(i % 10 == 0) {
				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * -800;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x,y,z),0,random.nextFloat() * 360,0,.75f));//atlas(use index)
			}
			
			if(i % 5 == 0) {
				float x = random.nextFloat() * 800;
				float z = random.nextFloat() * -800;
				float y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(tree, new Vector3f(x,y,z),0,random.nextFloat() * 360,0,.5f));
				x = random.nextFloat() * 800;
				z = random.nextFloat() * -800;
				y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(grass, new Vector3f(x,y,z),0,random.nextFloat() * 360,0,1));
				x = random.nextFloat() * 800;
				z = random.nextFloat() * -800;
				y = terrain.getHeightOfTerrain(x, z);
				entities.add(new Entity(flower, new Vector3f(x,y,z),0,random.nextFloat() * 360,0,.5f));
			}

		}
		
		Entity lampEntity = new Entity(lamp, new Vector3f(293, -6.8f, -305), 0, 0, 0, 1);
		entities.add(lampEntity);
		
		//renderer
		MasterRenderer renderer = new MasterRenderer(loader);
		
		//Player
		Player player = new Player(person, new Vector3f(150, 0, -170), 0, 0, 0, .5f);
		entities.add(player);
		
		//Camera
		Camera camera = new Camera(player);	
		
		//MousePicker
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
		
		//Water
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), fbos);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		WaterTile water = new WaterTile(110, -110, 0);
		waters.add(water);
		
		
		
		//GUI's
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		//GuiTexture healthBar = new GuiTexture(loader.loadTexture("health"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
		//guis.add(healthBar);
		
		//Logic
		while(!Display.isCloseRequested()){
			player.move(terrain);
			camera.move();
			picker.update();
			
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			/*
			System.out.println(picker.getCurrentRay());
			Vector3f terrainPoint = picker.getCurrentTerrainPoint();
			if(terrainPoint!=null) {
				lampEntity.setPosition(terrainPoint);
				lampLight.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y+15, terrainPoint.z));
			}
			*/
			//render reflection texture
			fbos.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, 1, 0, -water.getHeight() + 1f));
			camera.getPosition().y += distance;
			camera.invertPitch();
			
			//render refraction texture
			fbos.bindRefractionFrameBuffer();
			renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, -1, 0, water.getHeight() + 1f));
			
			//render to screen
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			fbos.unbindCurrentFrameBuffer();
			renderer.renderScene(entities, terrains, lights, camera, new Vector4f(0, 0, 0, 0));
			waterRenderer.render(waters,  camera, sun);
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}
		fbos.cleanUp();
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}
