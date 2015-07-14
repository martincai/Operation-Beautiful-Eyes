# Operation Beautiful Eyes
### Inspiration
Most children loves watching TV. Many of them like my daughter often wander too close to the TV screen. Because parents are not always around, there needs to be a way to prevent a child from watching TV from a close distance.
### How it will work / How it will be built
A Raspberry Pi board with camera is placed at where the TV screen is. It takes snapshots at a regular basis. Facial detection happens on the board. Once a snapshot contains a face, it gets uploaded to Azure storage, where facial recognition happens by leveraging Project Oxford. The Raspberry Pi board is also registered with an open IOT service running on Azure. Upon successful facial recognition of the child, parents get notified on the phone to take necessary actions, e.g. turn off the TV remotely.
