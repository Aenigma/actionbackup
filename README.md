# Action Backup

A quick and dirty backup utility that copies a directory into timestamped
directories.

This allows for backups to be restored pretty much *as is* without
needing to muck with specialized formats.

Backups are "rotated" using configurable values dictating how many copies to
keep.

## Why?

There are better tools for the job, of course. This tool doesn't conserve
space, at all, and falls flat in nearly every metric you can throw at it.

However, it's dead simple for a user because it has hardly any user interface
after it's set up, it's cross platform, and doesn't require integrations from
system  utilities like cron.