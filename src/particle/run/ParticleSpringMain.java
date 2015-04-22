package particle.run;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import particle.Particle;
import particle.force.ParticleGravityGenerator;
import particle.force.bungee.ParticleAnchoredBungee;
import particle.force.bungee.ParticleUnanchoredBungee;
import particle.force.spring.ParticleSpring;
import particle.force.spring.ParticleUnanchoredSpring;
import _math.Real;
import _math.Vector3D;

public class ParticleSpringMain extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final public static void main(String[] args ) {
		new ParticleSpringMain().run();
	}
	
	protected ParticleWorld world;
	protected Particle ball1;
	protected Particle ball2;
	protected PhysicsPanel pnlPhysics = new PhysicsPanel();
	
	public ParticleSpringMain() {
		this.world = new ParticleWorld();
		
		this.ball1 = new Particle( new Real( 10 ) , new Vector3D( new Real( 210 ) , new Real( 50 ), Real.ZERO ) , new Vector3D( new Real( 0 ) , new Real( -10 ) , Real.ZERO ) );
		this.ball2 = new Particle( new Real( 10 ) , new Vector3D( new Real( 270 ) , new Real( 70 ) , Real.ZERO ) , new Vector3D( new Real( 0 ) , Real.ZERO , Real.ZERO ) );
		this.ball1.setInverseMass( Real.ZERO );
		this.ball1.setDamping( new Real(0.999) );
		this.ball2.setDamping( new Real(0.999) );
		this.world.addParticle( this.ball1 );
		this.world.addParticle( this.ball2 );
		
		ParticleGravityGenerator gravityGenerator = new ParticleGravityGenerator();
		gravityGenerator.addObject( this.ball1 );
		gravityGenerator.addObject( this.ball2 );
		this.world.addForceGenerator( gravityGenerator );
		
		
		
		ParticleSpring spring = new ParticleUnanchoredSpring( this.ball1 , new Real( 20 ) , new Real( 50 ) );
		spring = new ParticleUnanchoredBungee( this.ball1 , new Real ( 5 ) , new Real( 45 ) );
		spring = new ParticleAnchoredBungee( new Vector3D( new Real( 210 ) , new Real( 50 ) , Real.ZERO ) , new Real( 20 ) , new Real( 100 ) );
		spring.addObject( this.ball2 );
		
		this.world.addForceGenerator( spring );
		
		/*Real dt = Real.ONE.divide( new Real( 1000 ) );
		for ( int i = 0; i < 10000; i++ ) {
			this.world.runPhysics( dt );
			if ( i % 100 == 0 ) {
				//System.out.println( dt.multiply( new Real(i) ) + " seconds : " + ball1 );
				//System.out.println( dt.multiply( new Real(i) ) + " seconds : " + ball2 );
				System.out.println( this.ball1.getVelocity().getY() );
			}
		}*/
		
		add( this.pnlPhysics );
		setSize( 500 , 500 );
		setVisible( true );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

	@Override
	public void run() {
		while ( true ) {
			Real dt = Real.ONE.divide( new Real ( 1000 ) );
			this.world.runPhysics( dt );
			this.pnlPhysics.repaint();
			//System.out.println( ball1 );
			//System.out.println( ball2 );
			
			try {
				Thread.sleep( 1 );
			} catch (Exception e) {
				//ignore
			}
		}
	}
	
	private class PhysicsPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public PhysicsPanel() {
			
		}
		
		
		@Override
		public void paint( Graphics graphics ) {
			super.paint( graphics );
			Graphics2D g = ( Graphics2D ) graphics;
			
			g.setColor( Color.black );
			//g.fillOval( (int) ParticleSpringMain.this.ball1.getPosition().getX().value() , (int) ParticleSpringMain.this.ball1.getPosition().getY().value(), 10 , 10 );
			g.setColor( Color.blue );
			g.fillOval( (int) ParticleSpringMain.this.ball2.getPosition().getX().value() , (int) ParticleSpringMain.this.ball2.getPosition().getY().value(), 10 , 10 );
			g.setColor( Color.red );
			g.drawLine( 210 , 50 , (int) ParticleSpringMain.this.ball2.getPosition().getX().value() + 5 , (int) ParticleSpringMain.this.ball2.getPosition().getY().value() + 5) ;
			//g.drawLine( (int) ParticleSpringMain.this.ball1.getPosition().getX().value() + 5 , (int) ParticleSpringMain.this.ball1.getPosition().getY().value() + 5, (int) ParticleSpringMain.this.ball2.getPosition().getX().value() + 5, (int) ParticleSpringMain.this.ball2.getPosition().getY().value() + 5);
		}
	}
}
