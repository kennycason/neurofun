# Neurofun

Render a constantly evolving neural network of functions. Color algorithm mutates alongside functions and function parameters.

<img src="https://raw.githubusercontent.com/kennycason/neurofun/refs/heads/main/samples/00000.png" width="50%"/><img src="https://raw.githubusercontent.com/kennycason/neurofun/refs/heads/main/samples/00001.png" width="50%"/>
<img src="https://raw.githubusercontent.com/kennycason/neurofun/refs/heads/main/samples/00002.png" width="50%"/><img src="https://raw.githubusercontent.com/kennycason/neurofun/refs/heads/main/samples/00003.png" width="50%"/>
<img src="https://raw.githubusercontent.com/kennycason/neurofun/refs/heads/main/samples/00004.png" width="50%"/><img src="https://raw.githubusercontent.com/kennycason/neurofun/refs/heads/main/samples/00005.png" width="50%"/>

## Dev Log Videos

- 

## Images to Video

### Youtube Quality

`ffmpeg -framerate 8 -pattern_type glob -i "*.png" -vf "scale=1280:720,fps=8" -c:v libx264 -b:v 5000k -crf 16 -pix_fmt yuv420p pokemon.mp4`

### Basic Quality

`ffmpeg -framerate 8 -pattern_type glob -i "*.png" -c:v libx264 -pix_fmt yuv420p pokemon.mp4`
