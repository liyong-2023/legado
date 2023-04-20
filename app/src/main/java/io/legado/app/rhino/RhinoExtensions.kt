@file:Suppress("unused")

package io.legado.app.rhino

import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import org.mozilla.javascript.ScriptableObject
import java.io.Reader

fun Context.evaluate(
    scope: Scriptable,
    source: String,
    sourceName: String = "<Unknown source>",
    lineno: Int = 1,
    securityDomain: Any? = null
): Any? {
    return Rhino.unwrapReturnValue(
        evaluateString(scope, source, sourceName, lineno, securityDomain)
    )
}

fun Context.evaluate(
    bindings: Bindings,
    source: String,
    sourceName: String = "<Unknown source>",
    lineno: Int = 1,
    securityDomain: Any? = null
): Any? {
    val scope = initStandardObjects()
    scope.putBindings(bindings)
    return Rhino.unwrapReturnValue(
        evaluateString(scope, source, sourceName, lineno, securityDomain)
    )
}

fun Context.evaluate(
    scope: Scriptable,
    reader: Reader,
    sourceName: String = "<Unknown source>",
    lineno: Int = 1,
    securityDomain: Any? = null
): Any? {
    return Rhino.unwrapReturnValue(
        evaluateReader(scope, reader, sourceName, lineno, securityDomain)
    )
}

fun Context.evaluate(
    bindings: Bindings,
    reader: Reader,
    sourceName: String = "<Unknown source>",
    lineno: Int = 1,
    securityDomain: Any? = null
): Any? {
    val scope = initStandardObjects()
    scope.putBindings(bindings)
    return Rhino.unwrapReturnValue(
        evaluateReader(scope, reader, sourceName, lineno, securityDomain)
    )
}

fun Scriptable.putBinding(key: String, value: Any?) {
    val wrappedOut = Context.javaToJS(value, this)
    ScriptableObject.putProperty(this, key, wrappedOut)
}

fun Scriptable.putBindings(bindings: Bindings) {
    bindings.forEach { (t, u) ->
        putBinding(t, u)
    }
}