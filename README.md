Yogurt.cloud
============

## Design

We design our service call and data tran

## Messages

All messages will be sent through UDP.

Every service call contain 3 messages.

* Request
* Response
* ACK Response

All messages have a shared format as below.

* Call ID: A unique 2 byte ID for a service call.
* EOM: Indicating if the message is the last packet for a message.
* Packet ID: A 4 byte integer representing the fragment ID for current message.
* ACK Packet ID: Next packet expected from sender, for replying message only.
* Packet Body: Data in the packet.

		    0                   1                   2                   3   
		    0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 
		   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		   |                               |E|                             |
		   |            Call ID            |O|          Reserved           |
		   |                               |M|                             |
		   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		   |                           Packet ID                           |
		   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		   |                         ACK Packet ID                         |
		   +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
		
		                               Header Format
		


A packet with EOM flag set is considered to be the last packet of the message. Receiving such packet means that the whole message has been transfered.

The server will buffer the response after receiving a request unless receiving ACK response or a ACK timeout.


### Packet Body Format

The beginning part of the packet will be headers, encoded in ASCII text. After the header, there will be an empty line, followed by message body.

The format of header is described as below.

First line:

	<API-VERSION> <MESSAGE-TYPE> <ARGUMENT*>

For examples:

	# For Request
	YC1 READ /path/to/file.txt
	YC1 INSERT /path/to/file.txt
	YC1 MONITOR /path/to/file.txt
	
	# For Response
	YC1 STATUS SUCCESS
	YC1 STATUS ERROR
	YC1 NOTICE

For remaining headers, they will be key-value pairs represented in the following format.

	<key>: <value>
	# Example
	Offset: 0
	Limit: 10


### Service Request Message

#### Request a File

Request:

	YC1 READ /path/to/file.txt
	Offset: 2  # Offset in bytes default to 0
	Limit: 5  # Maximum bytes returned in bytes

Response:

Success

	RC1 STATUS SUCCESS
	
	File content in binary

Error

	RC1 STATUS ERROR
	Exception: 404 File Not Found


#### Insert into a file

Request:

	YC1 INSERT /path/to/file.txt
	Offset: 5

	Content in binary

Response:

Success / ERROR

#### Monitor a file


Request:

	YC1 MONITOR /path/to/file.txt
	Interval: 3600  # Time in seconds that the client want to listen to the server.

Response:

	YC1 NOTICE

	New file content in binary.

### Constants

#### Timeout Duration










