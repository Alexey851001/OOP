import plugin.base32.PlugBase32;
import sample.BaseObj;

module pluginBase32 {

    requires program;
    requires org.apache.commons.codec;

    provides BaseObj with PlugBase32;
}