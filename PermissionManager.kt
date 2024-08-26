package com.example.helper.extras

import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

object PermissionManager {
    fun getForSinglePermission(activity: ComponentActivity, permission: String, onStatusChange: (status: Pair<String, Boolean>) -> Unit): SinglePermissionHandler {
        return SinglePermissionHandler.getInstance(activity, permission, onStatusChange)
    }

    fun getForMultiplePermissions(activity: ComponentActivity, permissions: List<String>, onStatusChange: (status: List<Pair<String, Boolean>>) -> Unit): MultiplePermissionHandler {
        return MultiplePermissionHandler.getInstance(activity, permissions, onStatusChange)
    }


    class SinglePermissionHandler {
        private lateinit var mActivity: ComponentActivity

        private lateinit var mPermission: String
        private lateinit var mOnStatusChange: (status: Pair<String, Boolean>) -> Unit

        private lateinit var requester: ActivityResultLauncher<String>

        private constructor()

        private constructor(activity: ComponentActivity, permission: String, onStatusChange: (status: Pair<String, Boolean>) -> Unit) {
            mActivity = activity
            mPermission = permission
            mOnStatusChange = onStatusChange

            requester = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (this::mOnStatusChange.isInitialized) {
                    mOnStatusChange.invoke(Pair(mPermission, it))
                }
            }
        }

        companion object {
            fun getInstance(activity: ComponentActivity, permission: String, onStatusChange: (status: Pair<String, Boolean>) -> Unit): SinglePermissionHandler {
                return SinglePermissionHandler(activity, permission, onStatusChange)
            }
        }

        fun request() {
            if (this::requester.isInitialized) {
                requester.launch(mPermission)
            }
        }

        // don't forget to call this function in onResume of activity
        fun check() {
            if (this::mOnStatusChange.isInitialized) {
                mOnStatusChange.invoke(Pair(mPermission, mActivity.checkSelfPermission(mPermission) == PackageManager.PERMISSION_GRANTED))
            }
        }
    }



    class MultiplePermissionHandler {
        private lateinit var mActivity: ComponentActivity

        private lateinit var mPermissions: List<String>
        private lateinit var mOnStatusChange: (status: List<Pair<String, Boolean>>) -> Unit

        private lateinit var requester: ActivityResultLauncher<Array<String>>

        private constructor()

        private constructor(activity: ComponentActivity, permissions: List<String>, onStatusChange: (status: List<Pair<String, Boolean>>) -> Unit) {
            mActivity = activity
            mPermissions = permissions
            mOnStatusChange = onStatusChange

            requester = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                if (this::mOnStatusChange.isInitialized && it.isNotEmpty()) {
                    mOnStatusChange.invoke(it.map { Pair(it.key, it.value) })
                }
            }
        }

        companion object {
            fun getInstance(activity: ComponentActivity, permissions: List<String>, onStatusChange: (status: List<Pair<String, Boolean>>) -> Unit): MultiplePermissionHandler {
                return MultiplePermissionHandler(activity, permissions, onStatusChange)
            }
        }

        fun request() {
            if (this::requester.isInitialized) {
                requester.launch(mPermissions.toTypedArray())
            }
        }

        // don't forget to call this function in onResume of activity
        fun check() {
            if (this::mOnStatusChange.isInitialized) {
                mOnStatusChange.invoke(mPermissions.map { Pair(it, mActivity.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED) })
            }
        }
    }
}