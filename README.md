s3routes
========
It's possible to define rules that describe to where client should be redirected when it access some,
specified object within Amazon S3 bucket.

This project represents a tool that does one simple thing - it converts that redirection rules from
human readable text format to valid XML.

For instance having plain text routing rules

    former.php                         new.html
    former-directory/                  new-directory/
    internal/page.html                 http://external.domain.com/index.html

this tool can give you valid XML ready to be inserted to AWS Console

    <?xml version="1.0"?>
    <RoutingRules>
        <RoutingRule>
            <Condition>
                <KeyPrefixEquals>former.php</KeyPrefixEquals>
            </Condition>
            <Redirect>
                <ReplaceKeyPrefixWith>new.html</ReplaceKeyPrefixWith>
            </Redirect>
        </RoutingRule>
        <RoutingRule>
            <Condition>
                <KeyPrefixEquals>former-directory/</KeyPrefixEquals>
            </Condition>
            <Redirect>
                <ReplaceKeyPrefixWith>new-directory/</ReplaceKeyPrefixWith>
            </Redirect>
        </RoutingRule>
        <RoutingRule>
            <Condition>
                <KeyPrefixEquals>internal/page.html</KeyPrefixEquals>
            </Condition>
            <Redirect>
                <HostName>external.domain.com</HostName>
                <Protocol>http</Protocol>
                <ReplaceKeyPrefixWith>index.html</ReplaceKeyPrefixWith>
            </Redirect>
        </RoutingRule>
    </RoutingRules>

The project is a part of article <a target="_blank" href="http://sukharevd.net/generating-routing-rules-for-amazon-s3-bucket.html">Generating routing rules for Amazon S3 bucket</a>. 

##Demo
Application is available online <a target="_blank" href="http://sukharevd.net/static/files/blog/s3routes/index.html">here</a>.
