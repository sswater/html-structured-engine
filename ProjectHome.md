## Introduction ##
Html-structured-engine is an engine to get URL, follow links, parse html to get structured record data. It is a crawler to get targeted contents directly, so it need not a big storage to save the whole html content.

## Configurable ##
It is configurable, with a set of rules and patterns, the engine can follow links to get data. It can get more records within one page, or combine data in more pages to one record, or get paged data from pages. The caller can pass-in parameters, headers or cookies etc.

You can pass in rules and patterns when you call html-structured-engine, or save the rules and patterns and refer it by alias name.

## Flexible ##
You can use html-structured-engine as a simple RPC caller, to get structured data from URL directory. Or use it as a standalone service, to perform the task in schedule, and get the saved data later.

## Frame ##
It has a little frame for choice. The frame can be used to set a timer to let the task perform in schedule. It can be used to save the data and detect data repetition. You can also not use the frame and use html-structured-engine as a simple RPC caller.

## License ##
Non-commercial license. This project is free for non-commercial use. If you need to use it for commercial use, please contact [webmaster@regexlab.com](mailto:webmaster@regexlab.com) for license and technique supports.