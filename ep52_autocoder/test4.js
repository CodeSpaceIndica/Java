
    for(let i=0; i<dataArray.length; i++) {
        let freq = (dataArray[i] - 128) / 2;õ

        let rIndex = parseInt( randomBetween(0, points.length-1) );
        let aPnt = points[rIndex];
        aPnt.x += randomBetween(-freq, freq);
        aPnt.y += randomBetween(-freq, freq);
    }
