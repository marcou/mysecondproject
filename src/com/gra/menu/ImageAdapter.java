package com.gra.menu;

import com.gra.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	
		private int width;
		private int height;
		
		/** The parent context */
		private Context myContext;
		// Put some images to project-folder: /res/drawable/
		// format: jpg, gif, png, bmp, ...
		private int[] myImageIds = {R.drawable.asteroids, R.drawable.background, R.drawable.gems};

		/** Simple Constructor saving the 'parent' context. */
		public ImageAdapter(Context c) {
			this.myContext = c;
			this.width = 100;
			this.height = 100;
		}
		
		public ImageAdapter(Context c, int width, int height) {
			this.myContext = c;
			this.width = width;
			this.height = height;
		}

		public int getImageId(int position){
			return myImageIds[position];
		}
		
		// inherited abstract methods - must be implemented
		// Returns count of images, and individual IDs
		public int getCount() {
			return this.myImageIds.length;
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}
		// Returns a new ImageView to be displayed,
		public View getView(int position, View convertView, 
				ViewGroup parent) {

			// Get a View to display image data 					
			ImageView iv = new ImageView(this.myContext);
			iv.setImageResource(this.myImageIds[position]);

			// Image should be scaled somehow
			//iv.setScaleType(ImageView.ScaleType.CENTER);
			//iv.setScaleType(ImageView.ScaleType.CENTER_CROP);			
			//iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
			//iv.setScaleType(ImageView.ScaleType.FIT_XY);
			//iv.setScaleType(ImageView.ScaleType.FIT_END);
			
			// Set the Width & Height of the individual images
			iv.setLayoutParams(new Gallery.LayoutParams(width, height));

			return iv;
		}
	}// ImageAdapter