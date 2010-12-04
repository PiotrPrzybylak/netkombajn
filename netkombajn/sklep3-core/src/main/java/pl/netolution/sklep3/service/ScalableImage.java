package pl.netolution.sklep3.service;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class ScalableImage {

	BufferedImage sourceImage;

	public ScalableImage(BufferedImage sourceImage) {
		super();
		this.sourceImage = sourceImage;
	}

	public BufferedImage scale(int maxWidth, int maxHeight) {
		double sx = (double) maxWidth / sourceImage.getWidth();
		double sy = (double) maxHeight / sourceImage.getHeight();
		double scale = sx < sy ? sx : sy;
		if (scale > 1) {
			// If image is smaller then requried size, we are not scaling it at
			// all.
			return sourceImage;
		}

		int newWidth = (int) (scale * sourceImage.getWidth());
		int newHeight = (int) (scale * sourceImage.getHeight());

		return resize(sourceImage, newWidth, newHeight);
	}

	private BufferedImage resize(Image sourceImage, int newWidth, int newHeight) {

		Image scaledImage = sourceImage.getScaledInstance(newWidth, newHeight, Image.SCALE_AREA_AVERAGING);

		BufferedImage destination = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = destination.createGraphics();
		g.drawImage(scaledImage, 0, 0, null);

		return destination;

	}

}
