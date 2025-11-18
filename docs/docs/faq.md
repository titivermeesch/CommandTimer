---
sidebar_position: 8
---
# FAQ

## Can I execute a task on restart only

Yes, by using [execution limits](configuration/others#execution-limits).

1. Set the execution limit to 1
2. Activate the feature "reset execution after restart"
3. Configure your [task](jargon.md#task) as you would do usually

## MyCommand plugin does not work with CommandTimer

Add `register: true` in your MyCommand config and restart/reload the server

## Can I donate?

If you would like to donate, this can be done through my PayPal account `titivermeesch@gmail.com`
