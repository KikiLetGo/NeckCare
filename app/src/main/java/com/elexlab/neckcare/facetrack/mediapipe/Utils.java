package com.elexlab.neckcare.facetrack.mediapipe;

import com.elexlab.neckcare.facetrack.Euler;
import com.elexlab.neckcare.facetrack.Face;
import com.elexlab.neckcare.facetrack.FacePose;
import com.elexlab.neckcare.facetrack.Vector3;
import com.elexlab.neckcare.misc.EasyLog;
import com.google.mediapipe.formats.proto.LandmarkProto;
import com.google.mediapipe.solutions.facemesh.FaceMeshConnections;
import com.google.mediapipe.solutions.facemesh.FaceMeshResult;

import java.util.List;

public class Utils {
    public static FacePose findFacePose(FaceMeshResult result, int i) {
        if(result.multiFaceLandmarks() == null || result.multiFaceLandmarks().size()<=0){
            return null;
        }
        List<LandmarkProto.NormalizedLandmark> faceLandmarkList = result.multiFaceLandmarks().get(i).getLandmarkList();
        Vector3 resultantVector = new Vector3();
        LandmarkProto.NormalizedLandmark noseLandmark = result.multiFaceLandmarks().get(0).getLandmarkList().get(1);
        Vector3 noseVec = new Vector3(noseLandmark.getX(), noseLandmark.getY(), noseLandmark.getZ());

        for (FaceMeshConnections.Connection c : FaceMeshConnections.FACEMESH_FACE_OVAL) {
            LandmarkProto.NormalizedLandmark start = faceLandmarkList.get(c.start());
            LandmarkProto.NormalizedLandmark end = faceLandmarkList.get(c.end());

            Vector3 p1 = new Vector3(start.getX(), start.getY(), start.getZ());
            Vector3 p2 = new Vector3(end.getX(), end.getY(), end.getZ());

            Vector3 vec1 = p1.sub(noseVec);
            Vector3 vec2 = p2.sub(noseVec);

            //vec1 叉乘 vec2得到这个三角面的垂直向量
            Vector3 forwardVec = vec2.crossProduct(vec1).normalize();
            resultantVector = resultantVector.add(forwardVec);

        }
        resultantVector = resultantVector.normalize().multiplyScalar(0.1f).add(noseVec);
        //float[] vertex = {noseVec.getX(), noseVec.getY(), resultantVector.getX(), resultantVector.getY()};

        FacePose facePose = new FacePose(noseVec,resultantVector);
        Euler euler = facePose.getAngle();
        EasyLog.v("findFacePose","====================================================");
        EasyLog.v("findFacePose","X:"+ (float) (180 / Math.PI * euler.getX()));
        EasyLog.v("findFacePose","y:"+ (float) (180 / Math.PI * euler.getY()));
        EasyLog.v("findFacePose","z:"+ (float) (180 / Math.PI * euler.getZ()));

        Vector3 headVec = findHeadVec(faceLandmarkList,facePose);
        facePose.setHeadVec(headVec);
        return facePose;
    }

    private static Vector3 findHeadVec(List<LandmarkProto.NormalizedLandmark> faceLandmarkLis, FacePose facePose) {
        int p1 = FaceMeshConnections.FACEMESH_LEFT_EYEBROW.asList().get(0).start();
        int p2 = FaceMeshConnections.FACEMESH_RIGHT_EYEBROW.asList().get(0).start();

        LandmarkProto.NormalizedLandmark v1 = faceLandmarkLis.get(p1);
        LandmarkProto.NormalizedLandmark v2 = faceLandmarkLis.get(p2);

        Vector3 vec1 = new Vector3(v1.getX(),v1.getY(),v1.getZ());
        Vector3 vec2 = new Vector3(v2.getX(),v2.getY(),v2.getZ());

        Vector3 planeVec = vec2.sub(vec1).normalize().multiplyScalar(0.1f).add(facePose.getNoseVec());

        Vector3 pVec1 = planeVec.sub(facePose.getNoseVec());
        Vector3 pVec2 = facePose.getForwardVec().sub(facePose.getNoseVec());

        Vector3 headVec = pVec2.crossProduct(pVec1).normalize();

        headVec = headVec.multiplyScalar(0.1f).add(facePose.getNoseVec());

        return headVec;
    }
}
