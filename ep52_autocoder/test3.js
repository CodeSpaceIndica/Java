
function dither() {
    ctx.fillStyle = "rgba(0, 0, 0, 0.1)";
    ctx.beginPath();
    ctx.rect(0, 0, width, height);
    ctx.fill();
õ

    ctx.fillStyle = "#FFFFFF";
    for(let i=0; i<points.length; i++) {
        let aPnt = points[i];

        ctx.beginPath();
        ctx.rect(aPnt.x, aPnt.y, PARTICLE_SIZE, PARTICLE_SIZE);
        ctx.fill();
    }

    requestAnimationFrame(dither);
}