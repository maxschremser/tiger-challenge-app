package test

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.schremser.challenger.app.CloudantProviderFactory
import com.schremser.challenger.providers.CloudantExpenseProvider
import org.junit.Test

/**
 * Created by bluemax on 12.07.17.
 */
class ImportCloudantDocuments extends GroovyTestCase {
    def json = """
[
    {"type":"car","description":"maxis description","date":1492770960000,"amount":710.59,"name":"Service 2017","id":"1","owner":"maxi"},
    {"type":"car","description":"another description","date":1492770960000,"amount":713.84,"name":"another Service 2017","id":"2","owner":"maxi"},
    {"type":"wine","description":"maxis description","date":1492792320000,"amount":623.42,"name":"Shopping Lui&Max","id":"3","owner":"maxi"},
    {"type":"wine","description":"another description","date":1492792320000,"amount":626.67,"name":"another Shopping Lui&Max","id":"4","owner":"maxi"},
    {"type":"pizza","description":"maxis description","date":1492976520000,"amount":110.0,"name":"Sebastiano","id":"5","owner":"maxi"},
    {"type":"pizza","description":"another description","date":1492976520000,"amount":113.25,"name":"another Sebastiano","id":"6","owner":"maxi"},
    {"type":"pizza","description":"maxis description","date":1493031000000,"amount":36.7,"name":"Latino (Suceava)","id":"7","owner":"maxi"},
    {"type":"pizza","description":"another description","date":1493031000000,"amount":39.95,"name":"another Latino (Suceava)","id":"8","owner":"maxi"},
    {"type":"bicycle","description":"maxis description","date":1493065380000,"amount":89.0,"name":"Fitnessland","id":"9","owner":"maxi"},
    {"type":"bicycle","description":"another description","date":1493065380000,"amount":92.25,"name":"another Fitnessland","id":"10","owner":"maxi"},
    {"type":"home","description":"maxis description","date":1493114400000,"amount":30.0,"name":"Waschmaschine","id":"11","owner":"maxi"},
    {"type":"home","description":"another description","date":1493114400000,"amount":33.25,"name":"another Waschmaschine","id":"12","owner":"maxi"},
    {"type":"home","description":"maxis description","date":1493224860000,"amount":20.0,"name":"Internet","id":"13","owner":"maxi"},
    {"type":"home","description":"another description","date":1493224860000,"amount":23.25,"name":"another Internet","id":"14","owner":"maxi"},
    {"type":"game-controller-a","description":"maxis description","date":1493376060000,"amount":43.21,"name":"Abschlussfeier","id":"15","owner":"maxi"},
    {"type":"game-controller-a","description":"another description","date":1493376060000,"amount":46.46,"name":"another Abschlussfeier","id":"16","owner":"maxi"},
    {"type":"phone-portrait","description":"new sim card from julia.","date":1492709890089,"amount":5.0,"name":"PrePaid SIM","id":"17","owner":"maxi"},
    {"type":"car","description":"some maxi description","date":1499883540000,"amount":45.37,"name":"some Service 2017","id":"18","owner":"maxi"},
    {"type":"game-controller-a","description":"some maxi description","date":1499824440000,"amount":30.71,"name":"some Shopping Lui&Max","id":"19","owner":"maxi"},
    {"type":"pizza","description":"some maxi description","date":1499838000000,"amount":99.51,"name":"some Sebastiano","id":"20","owner":"maxi"},
    {"type":"pizza","description":"some maxi description","date":1499848680000,"amount":59.24,"name":"some Latino (Suceava)","id":"21","owner":"maxi"},
    {"type":"bicycle","description":"some maxi description","date":1499822760000,"amount":93.05,"name":"some Fitnessland","id":"22","owner":"maxi"},
    {"type":"home","description":"some maxi description","date":1499885640000,"amount":53.79,"name":"some Waschmaschine","id":"23","owner":"maxi"},
    {"type":"home","description":"some maxi description","date":1499826960000,"amount":62.04,"name":"some Internet","id":"24","owner":"maxi"},
    {"type":"game-controller-a","description":"some maxi description","date":1499868780000,"amount":63.3,"name":"some Abschlussfeier","id":"25","owner":"maxi"}
]
"""

    def provider = (CloudantExpenseProvider)new CloudantProviderFactory().createExpenseProvider()

    @Test
    void testImportDocuments() {
        JsonArray array = (JsonArray) new JsonParser().parse(json);

        // Look for the VCAP key that holds the service info
        provider.all.each { item ->
            provider.DB.remove(item)
        }

        for (JsonObject object : array) {
            provider.DB.save(object)
            println "created $object.type - $object.name"
        }

        assert 1 == 1

    }

}
