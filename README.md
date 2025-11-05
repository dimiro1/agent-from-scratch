# Building an Agent from Scratch

This repository contains the source code for my blog series on building a terminal agent in Clojure.

## Blog Posts

- **Part 1:** [Building an Agent from Scratch](https://dimiro1.dev/building-an-agent-from-scratch)
- **Part 2:** [The Conversation Loop](https://dimiro1.dev/the-conversation-loop)

## Repository Structure

Each folder contains the complete code for that part of the series:

- `01/` - Basic OpenAI API client and first request
- `02/` - Conversation loop with message history
- `03/` - Markdown rendering with ANSI escape codes

## Running the Code

Navigate to any folder and run:

```bash
# Set your OpenAI API key
export OPENAI_API_KEY=your-key-here

# Run the agent
clj -M:run
```

Type `exit` to quit the conversation.
