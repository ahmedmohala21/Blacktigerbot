
package com.blacktiger.bot

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class GameAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // استقبال أحداث اللعبة
    }

    override fun onInterrupt() {
        // إيقاف الخدمة عند المقاطعة
    }
}
