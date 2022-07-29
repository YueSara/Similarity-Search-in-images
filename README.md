## Finds most similar images given a query image with OpenCV

based on L2 or Hamming distance

- ImageCreation: rotates images by 90°, 180°, 270°, mirrors images by x and y axis
- FD (Feature Detection): detects features & descriptors in images, depending on run might use kmeans clustering on descriptors, matches them to query image, returns 10 most similar images
			    
- Skript: takes query image path, automatically runs all experiments: Hamming distance or L2, no clustering, clustering with k=75 or k=100, 3 iterations per combination
