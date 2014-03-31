package ltg.ns.ambient.pollers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import ltg.ns.ambient.model.Image;

import com.fasterxml.jackson.databind.JsonNode;

public class ImagePoller extends Poller implements Runnable {
	
	private Set <Image> images = Collections.synchronizedSet(new HashSet<Image>());

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			Set<Image> new_images = new HashSet<>();
			// Fetch all images
			new_images.addAll(parseImages(null));
			// Check if anything new happened, update notes and notify observers
			if (!newAndOldImagesAreTheSame(new_images)) {
				updateNotes(new_images);
				// Notify observers
				this.setChanged();
				this.notifyObservers(new_images);
			}
			// Sleep...
			try {
				Thread.sleep(POLL_INTERVAL);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
	
	
	private Set<Image> parseImages(JsonNode new_images_from_get) {
		Set<Image> images = new HashSet<>();
		// TODO remove this fake data!
		images.add(new Image());
		return images;
	}


	private boolean newAndOldImagesAreTheSame(Set<Image> new_images) {
		return images.containsAll(new_images);
	}


	private void updateNotes(Set<Image> new_images) {
		images.addAll(new_images);
	}

}
