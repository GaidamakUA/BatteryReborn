package com.blogspot.androidgaidamak;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class BatteryGame extends ApplicationAdapter {
	public static final int VIEWPORT_WIDTH = 800;
	public static final int VIEWPORT_HEIGHT = 480;
	protected SpriteBatch batch;
	protected ShapeRenderer shapeRenderer;
	protected BitmapFont font;


	public boolean exception;
	public Exception firstException;
	protected volatile boolean initialized;
	protected volatile boolean initInProgress;

	protected volatile Thread thread;
	private OrthographicCamera camera;

	protected void initialize() {
	}

	;

	public void init() {
		exception = false;
		firstException = null;
		initialized = false;
		initInProgress = false;
		thread = null;
	}

	public void paint() {
	}

	public void exception(Exception e) {
		if (!exception) {
			exception = true;
			firstException = e;
			e.printStackTrace();
		}
	}

	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		batch = new SpriteBatch();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fixedsys-ligatures.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 10;
		font = generator.generateFont(parameter);
		shapeRenderer = new ShapeRenderer();
		initialize();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		batch.setProjectionMatrix(camera.combined);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		batch.begin();
		paint();
		batch.end();
		shapeRenderer.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}
