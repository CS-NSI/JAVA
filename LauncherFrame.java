package fr.wildcraft.launcher;

import javax.swing.JFrame;

import fr.theshark34.openlauncherlib.util.CrashReporter;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.util.WindowMover;

@SuppressWarnings("serial")
public class LauncherFrame extends JFrame {
	
	private static LauncherFrame instance;
	private LauncherPanel launcherPanel;
	private static CrashReporter crashreporter;
	
	public LauncherFrame( ) {
		this.setTitle("WildCraft Launcher");
		this.setSize(975, 625);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		this.setIconImage(Swinger.getResource("favicon.png"));
		this.setContentPane(launcherPanel = new LauncherPanel());
		
		WindowMover mover = new WindowMover(this);
		this.addMouseListener(mover);
		this.addMouseMotionListener(mover);
		
		
		this.setVisible(true);
	}

	public static void main(String[] args) {
		Swinger.setSystemLookNFeel();
		Swinger.setResourcePath("/fr/wildcraft/launcher/resources/");
		Launcher.WC_CRASH_FOLDER.mkdirs();
		crashreporter = new CrashReporter("WildCraft Launcher", Launcher.WC_CRASH_FOLDER);
		
		instance = new LauncherFrame();
	}
	
	public static LauncherFrame getInstance() {
		return instance;
	}
	
	
	public LauncherPanel getLauncherPanel() {
		return this.launcherPanel;
	}

	public static CrashReporter getCrashReporter() {
		return crashreporter;
	}
}
