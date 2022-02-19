package software.uniqore.storedemo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import software.uniqore.storedemo.data.datasource.StoreDemoDataSource
import software.uniqore.storedemo.data.datasource.memory.MemoryDataSource

@Configuration
@ComponentScan("software.uniqore.storedemo")
class StoreDemoConfiguration {

    @Bean
    fun provideDataSource(): StoreDemoDataSource = MemoryDataSource()

}