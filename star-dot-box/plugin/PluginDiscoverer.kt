package voidthinking.star.box.plugin

import java.util.ServiceLoader

object PluginDiscoverer {
    val plugins: List<StarDotBoxPlugin> = ServiceLoader.load(StarDotBoxPlugin::class.java).toList()
}