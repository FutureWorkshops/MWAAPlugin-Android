package com.futureworkshops.mobileworkflow.plugin.aa.view

import android.content.Context
import com.aatechintl.aaprintscanner.ActivityHandler
import com.aatechintl.aaprintscanner.ActivityHandler.TextStrings
import com.futureworkshops.mobileworkflow.plugin.aa.R
import java.util.*
import javax.inject.Inject

class FingerPrintCaptureProvider @Inject constructor() {

    fun populateTextStrings(
        context: Context,
        textStrings: TextStrings,
        isRightToLeft: Boolean = false
    ) {
        textStrings.TEXT_RIGHT_TO_LEFT = isRightToLeft
        textStrings.TEXT_NEW_SUBJECT = context.getString(R.string.fingerprint_sdk_new_subject)
        textStrings.TEXT_CANCEL_YES = context.getString(R.string.fingerprint_sdk_cancel_yes)
        textStrings.TEXT_CANCEL_NO = context.getString(R.string.fingerprint_sdk_cancel_no)
        textStrings.PREFIX_RESCAN = context.getString(R.string.fingerprint_sdk_prefix_rescan)
        textStrings.PREFIX_LEFT = context.getString(R.string.fingerprint_sdk_prefix_left)
        textStrings.PREFIX_RIGHT = context.getString(R.string.fingerprint_sdk_prefix_right)
        textStrings.PREFIX_LEFTABV = context.getString(R.string.fingerprint_sdk_prefix_left_abv)
        textStrings.PREFIX_RIGHTABV = context.getString(R.string.fingerprint_sdk_prefix_right_abv)
        textStrings.BASE_THUMB = context.getString(R.string.fingerprint_sdk_base_thumb)
        textStrings.BASE_INDEX = context.getString(R.string.fingerprint_sdk_base_index)
        textStrings.BASE_MIDDLE = context.getString(R.string.fingerprint_sdk_base_middle)
        textStrings.BASE_RING = context.getString(R.string.fingerprint_sdk_base_ring)
        textStrings.BASE_LITTLE = context.getString(R.string.fingerprint_sdk_base_little)
        textStrings.BASE_IM = context.getString(R.string.fingerprint_sdk_base_im)
        textStrings.BASE_RL = context.getString(R.string.fingerprint_sdk_base_rl)
        textStrings.BASE_4FINGER = context.getString(R.string.fingerprint_sdk_base_4_finger)
        textStrings.MESSAGE_CANCEL_CONFIRM =
            context.getString(R.string.fingerprint_sdk_msg_cancel_confirm)
        textStrings.MESSAGE_BG_ISSUE = context.getString(R.string.fingerprint_sdk_msg_bg_issue)
        textStrings.MESSAGE_BG_CHANGED = context.getString(R.string.fingerprint_sdk_msg_bg_changed)
        textStrings.MESSAGE_FINGER_TOOCLOSE =
            context.getString(R.string.fingerprint_sdk_msg_finger_too_close)
        textStrings.MESSAGE_FINGER_TOOFAR =
            context.getString(R.string.fingerprint_sdk_msg_finger_too_far)
        textStrings.MESSAGE_FINGER_NOTINPOSITION =
            context.getString(R.string.fingerprint_sdk_msg_finger_not_in_position)
        textStrings.MESSAGE_FINGER_OUTSIDE =
            context.getString(R.string.fingerprint_sdk_msg_finger_outside)
        textStrings.MESSAGE_FINGER_EXIT =
            context.getString(R.string.fingerprint_sdk_msg_finger_exit)
        textStrings.MESSAGE_FINGER_ENTER =
            context.getString(R.string.fingerprint_sdk_msg_finger_enter)
        textStrings.STATUS_SCANNING = context.getString(R.string.fingerprint_sdk_status_scanning)
        textStrings.STATUS_PROCESSING =
            context.getString(R.string.fingerprint_sdk_status_processing)
        textStrings.STATUS_RESCANNING =
            context.getString(R.string.fingerprint_sdk_status_rescanning)
        textStrings.STATUS_COMPLETED = context.getString(R.string.fingerprint_sdk_status_completed)
    }

    fun getFingerTypeFromOrdinalId(id: Int): ActivityHandler.CaptureProfile.FingerType = when (id) {
        ActivityHandler.CaptureProfile.FingerType.RT.ordinal -> {
            ActivityHandler.CaptureProfile.FingerType.RT
        }
        ActivityHandler.CaptureProfile.FingerType.RI.ordinal -> {
            ActivityHandler.CaptureProfile.FingerType.RI
        }
        ActivityHandler.CaptureProfile.FingerType.RM.ordinal -> {
            ActivityHandler.CaptureProfile.FingerType.RM
        }
        ActivityHandler.CaptureProfile.FingerType.RR.ordinal -> {
            ActivityHandler.CaptureProfile.FingerType.RR
        }
        ActivityHandler.CaptureProfile.FingerType.RL.ordinal -> {
            ActivityHandler.CaptureProfile.FingerType.RL
        }
        ActivityHandler.CaptureProfile.FingerType.LT.ordinal -> {
            ActivityHandler.CaptureProfile.FingerType.LT
        }
        ActivityHandler.CaptureProfile.FingerType.LI.ordinal -> {
            ActivityHandler.CaptureProfile.FingerType.LI
        }
        ActivityHandler.CaptureProfile.FingerType.LM.ordinal -> {
            ActivityHandler.CaptureProfile.FingerType.LM
        }
        ActivityHandler.CaptureProfile.FingerType.LR.ordinal -> {
            ActivityHandler.CaptureProfile.FingerType.LR
        }
        ActivityHandler.CaptureProfile.FingerType.LL.ordinal -> {
            ActivityHandler.CaptureProfile.FingerType.LL
        }
        else -> {
            ActivityHandler.CaptureProfile.FingerType.Null
        }
    }

    fun getFingerPositionNameFromFingerType(
        context: Context,
        type: ActivityHandler.CaptureProfile.FingerType
    ): String = when (type) {
        ActivityHandler.CaptureProfile.FingerType.RT -> {
            context.getString(R.string.confirm_finger_rt)
        }
        ActivityHandler.CaptureProfile.FingerType.RI -> {
            context.getString(R.string.confirm_finger_ri)
        }
        ActivityHandler.CaptureProfile.FingerType.RM -> {
            context.getString(R.string.confirm_finger_rm)
        }
        ActivityHandler.CaptureProfile.FingerType.RR -> {
            context.getString(R.string.confirm_finger_rr)
        }
        ActivityHandler.CaptureProfile.FingerType.RL -> {
            context.getString(R.string.confirm_finger_rl)
        }
        ActivityHandler.CaptureProfile.FingerType.LT -> {
            context.getString(R.string.confirm_finger_lt)
        }
        ActivityHandler.CaptureProfile.FingerType.LI -> {
            context.getString(R.string.confirm_finger_li)
        }
        ActivityHandler.CaptureProfile.FingerType.LM -> {
            context.getString(R.string.confirm_finger_lm)
        }
        ActivityHandler.CaptureProfile.FingerType.LR -> {
            context.getString(R.string.confirm_finger_lr)
        }
        ActivityHandler.CaptureProfile.FingerType.LL -> {
            context.getString(R.string.confirm_finger_ll)
        }
        else -> {
            ""
        }
    }

    fun getOrderFromFingerType(
        type: ActivityHandler.CaptureProfile.FingerType
    ): Int = when (type) {
        ActivityHandler.CaptureProfile.FingerType.LT -> 0
        ActivityHandler.CaptureProfile.FingerType.LI -> 1
        ActivityHandler.CaptureProfile.FingerType.LM -> 2
        ActivityHandler.CaptureProfile.FingerType.LR -> 3
        ActivityHandler.CaptureProfile.FingerType.LL -> 4
        ActivityHandler.CaptureProfile.FingerType.RT -> 5
        ActivityHandler.CaptureProfile.FingerType.RI -> 6
        ActivityHandler.CaptureProfile.FingerType.RM -> 7
        ActivityHandler.CaptureProfile.FingerType.RR -> 8
        ActivityHandler.CaptureProfile.FingerType.RL -> 9
        else -> -1
    }

    fun generateSubjectId(): String = UUID.randomUUID().toString()

}