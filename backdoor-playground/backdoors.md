## Open api route with backdoored handler that runs terminal command
``Applications.java``

Start a backdoored controller ate /api/backdoor

``api/user/BackdoorController.java``

This controller behave like the LoginController except for the lines that call the payload.

``utils/PayloadUsesExec.java``
``utils/PayloadProcessBuilder.java``

Both are used to execute commands at the terminal. This payload use ProcessBuilder/Executors to call the "ls" command. It can be modified to call some harmful command.


``utils/StreamGobbler.java``

StreamGobbler is used to handle the output of a system process executed through the ProcessBuilder. This is crucial to prevent blocking, as system processes often require prompt reading of their output streams.

## Leak data for "leak" user at login

``api/user/LoginController.java``

Get data from UserDataSource. If user is "leak", returns the leak data.

``data/datasource/UserDataSource.java``

Execute getAllUsersBackdoor() and returns String.

``data/respository/UserRepository.java``

Execute "SELECT * FROM app_user" and return String with data.