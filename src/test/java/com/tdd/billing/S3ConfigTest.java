package com.tdd.billing;

import com.amazonaws.services.s3.AmazonS3;
import com.tdd.billing.config.S3Config;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = S3Config.class)
@TestPropertySource(properties = {
        "aws.accessKeyId=testAccessKey",
        "aws.secretKey=testSecretKey",
        "aws.region=us-east-1"
})
class S3ConfigTest {

    @Autowired
    private AmazonS3 amazonS3;

    @Test
    void amazonS3BeanIsCreated() {
        assertThat(amazonS3).isNotNull();
    }
}
