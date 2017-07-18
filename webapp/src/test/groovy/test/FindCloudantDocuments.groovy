package test

import com.schremser.challenger.app.CloudantProviderFactory
import com.schremser.challenger.providers.CloudantExpenseProvider
import org.junit.Test

/**
 * Created by bluemax on 12.07.17.
 */
class FindCloudantDocuments extends GroovyTestCase {
    def provider = (CloudantExpenseProvider)new CloudantProviderFactory().createExpenseProvider()

    @Test
    void testFindAllDocs() {
        def count = provider.count()
        assert count == 25
    }

    void testFind() {
        // provider.getExpensesByType()
    }

}
