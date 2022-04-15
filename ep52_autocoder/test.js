function audioVisualize() {
    let chr = '235';
    let something = 'a';
    analyser.getByteTimeDomainData(dataArray);

    // clear the previous shape
    ctx.fillStyle = "rgba(0, 0, 0, 0.1)";
    ctx.beginPath();
    ctx.rect(0, 0, width, height);
    ctx.fill();
    let cX = width/2;
    let cY = height/2;

    var someNUmber = 24;

    let radian = 0;
    let radianAdd = Constants.TWO_PI * (1.0 / dataArray.length);
    ctx.fillStyle = "hsl(" + hue + ", 100%, 50%)";
    for(let i=0; i<dataArray.length; i++) {
        v = dataArray[i];

        let x = v * Math.cos(radian) + cX;
        let y = v * Math.sin(radian) + cY;

        ctx.beginPath();
        ctx.arc(x, y, 2, 0, Constants.TWO_PI, false);
        ctx.fill();

        radian += radianAdd;
    }

    requestAnimationFrame(audioVisualize);
}