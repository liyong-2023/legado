package io.legado.app.utils

import io.legado.app.constant.AppPattern.EXP_PATTERN
import io.legado.app.rhino.Bindings
import io.legado.app.rhino.Rhino
import io.legado.app.rhino.evaluate

object JsUtils {

    fun evalJs(js: String, bindingsFun: ((Bindings) -> Unit)? = null): String {
        val bindings = Bindings()
        bindingsFun?.invoke(bindings)
        if (js.contains("{{") && js.contains("}}")) {
            val sb = StringBuffer()
            val expMatcher = EXP_PATTERN.matcher(js)
            while (expMatcher.find()) {
                val result = expMatcher.group(1)?.let {
                    Rhino.use {
                        evaluate(bindings, it)
                    }
                } ?: ""
                if (result is String) {
                    expMatcher.appendReplacement(sb, result)
                } else if (result is Double && result % 1.0 == 0.0) {
                    expMatcher.appendReplacement(sb, String.format("%.0f", result))
                } else {
                    expMatcher.appendReplacement(sb, result.toString())
                }
            }
            expMatcher.appendTail(sb)
            return sb.toString()
        }
        return Rhino.use {
            evaluate(bindings, js)
        }.toString()
    }


}