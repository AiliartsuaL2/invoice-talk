package com.hocheoltech.invoicetalk.global.config

import com.hocheoltech.invoicetalk.global.utils.getCurrentUsername
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.sql.DataSource


@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["com.hocheoltech.invoicetalk"],
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "transactionManager",
)
class JpaConfig {
    @Bean
    fun auditorProvider() = AuditorAwareImpl()

    @Primary
    @Bean(name = ["entityManagerFactory"])
    fun entityManagerFactory(dataSource: DataSource): LocalContainerEntityManagerFactoryBean {
        return LocalContainerEntityManagerFactoryBean().apply {
            this.dataSource = dataSource
            setPackagesToScan("com.hocheoltech.invoicetalk")
            persistenceUnitName = "default"
            jpaVendorAdapter = HibernateJpaVendorAdapter()
            setJpaProperties(buildJpaProperties())
        }
    }

    private fun buildJpaProperties(): Properties {
        return Properties().apply {
            setProperty(
                "hibernate.physical_naming_strategy",
                "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy",
            )
            setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect")
            setProperty("hibernate.format_sql", "false")
            setProperty("hibernate.show_sql", "false")
            // 경각심을 위해 주석
            // setProperty("hibernate.default_batch_fetch_size", "1000")
        }
    }
}

class AuditorAwareImpl : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        val id = getCurrentUsername()
            ?: return Optional.of("SYSTEM")

        return Optional.of(id)
    }
}
