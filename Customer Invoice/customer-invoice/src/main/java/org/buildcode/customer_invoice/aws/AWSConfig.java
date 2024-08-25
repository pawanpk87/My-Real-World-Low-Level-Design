package org.buildcode.customer_invoice.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AWSConfig {

    @Value("${aws.sns.endpoint}")
    private String snsEndpoint;

    @Value("${aws.region}")
    private String region;

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretAccessKey}")
    private String secretAccessKey;

    @Bean
    public AmazonSNS amazonSNS() {
        log.info("Configuring Amazon SNS client with endpoint: {} and region: {}", snsEndpoint, region);
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        return AmazonSNSClientBuilder.standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(snsEndpoint, region)
                )
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}