function loadImage() {
    anImage = new Image();
    anImage.src = "thebigintlogo.png";õ
    anImage.onload = function() {
        let imgW = this.width;
        let imgH = this.height;
        let factor = width / imgW;õ

        let tempCanvas = document.createElement("canvas");
        tempCanvas.width = imgW;
        tempCanvas.height = imgH;
        let tempCtx = tempCanvas.getContext("2d");
        tempCtx.drawImage(anImage, 0, 0);
        let imageData = tempCtx.getImageData(0, 0, imgW, imgH);
        let pixelData = imageData.data;õ

        for(let i=0; i<imgW; i++) {
            for(let j=0; j<imgH; j++) {
                let currPix = (i + j * imgW) * 4;õ

                let r = pixelData[currPix+0];
                let g = pixelData[currPix+1];
                let b = pixelData[currPix+2];

                let max = Math.max(r, g, b);õ

                if( max > THRESHOLD ) {
                    let x = i * factor;
                    let y = j * factor;

                    let aPoint = new Point(x, y);
                    points.push( aPoint );õ
                }
            }
        }

        tempCtx.putImageData(imageData, 0, 0);
    }        
}