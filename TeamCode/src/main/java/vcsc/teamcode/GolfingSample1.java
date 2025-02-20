package vcsc.teamcode;

import vcsc.core.abstracts.action.Action;
import vcsc.core.abstracts.action.ActionBuilder;
import vcsc.teamcode.actions.intake.PreGrabPoseSpecimenGround;
import vcsc.teamcode.component.arm.elbow.ElbowState;
import vcsc.teamcode.component.arm.ext.ArmExtPose;
import vcsc.teamcode.component.arm.ext.ArmExtState;
import vcsc.teamcode.component.arm.ext.actions.SetExtPose;
import vcsc.teamcode.component.arm.rot.ArmRotState;
import vcsc.teamcode.component.claw.ClawPose;
import vcsc.teamcode.component.claw.ClawState;

public class GolfingSample1 implements Action {
    ArmExtState extState;
    ClawState clawState;
    ElbowState elbowState;
    ActionBuilder seq;
    SetExtPose slidesIn;
    SetExtPose slidesOut;
    ClawPose clawPose;
    PreGrabPoseSpecimenGround preGrabPose;

    public GolfingSample1(ArmRotState rotState, ArmExtState extState, ClawState clawState, PreGrabPoseSpecimenGround preGrabPose) {
        this.extState = extState;
        this.clawState = clawState;
        this.preGrabPose = preGrabPose;

        slidesIn = new SetExtPose(extState, ArmExtPose.RETRACT);
        slidesOut = new SetExtPose(extState, ArmExtPose.INTAKE);

        seq = new ActionBuilder();
    }
        @Override
        public void start() {
            seq = new ActionBuilder();

            seq.then(slidesOut)
                .then(preGrabPose);
            seq.start();
        }

        @Override
        public void loop() {
            seq.loop();
        }

        @Override
        public boolean isFinished() {
            return seq.isFinished();
        }

        @Override
        public void cancel() {
            seq.loop();
        }
    }

