
    let dist = getDistanceNoSqrt(aPnt.x, aPnt.y, mouseX, mouseY);
    if( dist < deflectionDistanceSqrd ) {
        //Remember angle is in radians
        let angle = getAngle(mouseX, mouseY, aPnt.x, aPnt.y);õ
        angle -= Math.PI;
        aPnt.x = Math.cos(angle) * deflectionDistance + mouseX;
        aPnt.y = Math.sin(angle) * deflectionDistance + mouseY;
    }
õ

    if( !particlePoint.equals(particleAnchor) ) {
        let dst = getDistanceNoSqrt(particlePoint.x, particlePoint.y, 
            particleAnchor.x, particleAnchor.y);
        let speed = dst / 10;
        let norm = speed / dst;õ
        let dX = (particleAnchor.x - particlePoint.x) * norm;
        let dY = (particleAnchor.y - particlePoint.y) * norm;
        particlePoint.x += dX;
        particlePoint.y += dY;õ
        if( dst < SNAP_DISTANCE_SQRD ) {
            particlePoint.x = particleAnchor.x;
            particlePoint.y = particleAnchor.y;
        }
    }
