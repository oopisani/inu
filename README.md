<p align="center">
  <img src="assets/inu_logo.png" alt="Automação Java" width="300"/>
</p>
<p align="center">
 <strong> The watchdog of your scripts </strong>
</p>
<p align="center">
  <img src="https://img.shields.io/badge/Java-24-blue" />
  <img src="https://img.shields.io/badge/OS-Windows%20%7C%20Linux-lightgrey" />
  <img src="https://img.shields.io/badge/Interface-CLI-black" />
  <img src="https://img.shields.io/badge/status-active%20development-yellow" />
</p>

## About me
<p><strong>inu</strong> is a lightweight automation and orchestration framework built with
native Java, Apache Commons libraries, and Log4j2, focused on workflow
traceability, structured logging, and centralized script management.
The project is designed around an evolving automation architecture,
including a custom command parsing system and unified multi-language
script orchestration.</p>


## Project Status

<strong>inu is currently under active development.
Core functionality is operational, but major architectural improvements,
refactors, and stability changes are still ongoing.</strong>

## Core Features

- Script Visualization: View scripts by tag folders, pending status, or across the entire workspace.
- Execution Modes: Dual mode system with both interactive CLI and direct CLI commands.
- Dynamic Logging: Weekly structured logs tracking all executions, errors, and system events.
- Windows Task Scheduling: Native scheduling support without requiring administrator privileges.
- Workspace Synchronization: Automatic setup and intelligent organization of the workspace environment.
- Raw-to-Inbox Pipeline: Simple ingestion system where scripts dropped into `raw` are validated and routed to `inbox`.
- Multi-Language Support: Executes and manages Python, PowerShell, and Shell scripts in a unified workspace.

## Requirements

* **Operating System:** Windows 10, Windows 11 or Linux.  
* **Python, PowerShell, or Shell environments depending on the scripts being executed.**

## First use

a. On first execution, inu automatically creates the `inu-workspace`
directory inside `user.home`, including the logging structure required
for execution traceability.

b. To initialize the operational workspace structure, run:

`init`

c. This will create directories such as:

- `raw`
- `inbox`
- `scripts`
- `organizational tag folders`

d. After initialization, scripts can be manually placed into the `raw`
folder and processed through the synchronization pipeline using:

`sync`

## Daily use
a. How to move a script to a tag folder:  
Interactive CLI Mode: Enter `move`  
Input: `scriptname.extension tagfoldername`  
CLI mode: `java -jar app\inu.jar move scriptname.extension tagfoldername`

b. How to run a script:  
Interactive CLI Mode: Enter `run`  
Input: `scriptname.extension tagfoldername`  
CLI mode: `java -jar app\inu.jar run scriptname.extension tagfoldername`

c. How to delete a script:  
Interactive CLI Mode: Enter `del`  
Input: `scriptname.extension tagfoldername`  
CLI mode: `java -jar app\inu.jar del scriptname.extension tagfoldername`

d. How to schedule a script (task):  
Interactive CLI Mode: Enter `task`  
Input: `scriptname.extension tagfoldername timing frequency`  
CLI mode: `java -jar app\inu.jar task scriptname.extension tagfoldername timing frequency`

e. How to view help menu:  
  **Interactive CLI Mode:** Input `help`  
  **CLI Mode:** `java -jar app\inu.jar help`
  
f. How to list scripts (3 ways):

- To view all scripts across all folders:  
  **Interactive CLI Mode:** Enter `list` → Input: `all`  
  **CLI Mode:** `java -jar app\inu.jar list all`

- To view only pending scripts in inbox:  
  **Interactive CLI Mode:** Enter `list` → Input: `pending`  
  **CLI Mode:** `java -jar app\inu.jar list pending`

- To view scripts in a specific tag folder:  
  **Interactive CLI Mode:** Enter `list` → Input: `tag` → `tagfoldername`  
  **CLI Mode:** `java -jar app\inu.jar list tag tagfoldername`
  
  
## Flow diagram:
<p align="center">
  <a href="https://mermaid.live/edit#pako:eNp1lFFv2jAUhf_KlaWunRQqEtKA_DApLSlNVwKCVJta-uAlBqwGO7Kdlhbx3-c4wEbX5SXxyfE9382NvEGZyCnCaF6I12xJpIa0P-NgrpMTiNJwHIKLYZqGk7SRw7NHuFdUAl3TrNJUAePV01dotb7B5SNM35SmK8gkJbt3rVchn1VJMvrUVPhQ3jPlo_R-3MiXttDVPkRWXMEp40yfPn1tHFfW0f8nSpJXx-T9EmsHVCZZqRUQnoMmC5iLIqdSfQ7QwRAng2iaxqMEzibhj11Q3wZFBmVIeEUKmGpa4oYrl6JUhxjGtajz94gfAnwM43gc3cVJZOuDKRsnl6OfO3tkg66Pe1ZvPDv0fG0dg0PPL6Rgue36C6zEi7mTSi-FZO80P1AZJvs5Pu_6AsNoMgiT-CFsGrdENVoaDqa73IHNvTkmqwMPZDfWER_IGpo9wlyKVQNR0-RUMUntRP4zigBDMpoMwzu4n4aDqHkb24jbTSLkykxhSHkFo5JKopngavt3oVtr_W54DeoesRHvjJjT4lgcGlET9XysJkYtmNLH6sioS1qUx-rYqHTNrBc5aCFZjrCWFXXQihreeok29Y4Z0ku6ojOEzWNO5PMMzfjW7CkJfxBitd8mRbVYIjwnhTKrqqzn3GdkIckfC-Xmd74SFdcIu92uZ4sgvEFrhL2Of-5ddHpur931Ou3A6zroDeGW5_fOA8_1AtcNOj2_HWwd9G5zXeMPuoEfXLQ7vt_ze4GDaM60kMPmcLBnxPY3ZLg1AA">
    <img 
      src="https://mermaid.ink/img/pako:eNp1lFFv2jAUhf_KlaWunRQqEtKA_DApLSlNVwKCVJta-uAlBqwGO7Kdlhbx3-c4wEbX5SXxyfE9382NvEGZyCnCaF6I12xJpIa0P-NgrpMTiNJwHIKLYZqGk7SRw7NHuFdUAl3TrNJUAePV01dotb7B5SNM35SmK8gkJbt3rVchn1VJMvrUVPhQ3jPlo_R-3MiXttDVPkRWXMEp40yfPn1tHFfW0f8nSpJXx-T9EmsHVCZZqRUQnoMmC5iLIqdSfQ7QwRAng2iaxqMEzibhj11Q3wZFBmVIeEUKmGpa4oYrl6JUhxjGtajz94gfAnwM43gc3cVJZOuDKRsnl6OfO3tkg66Pe1ZvPDv0fG0dg0PPL6Rgue36C6zEi7mTSi-FZO80P1AZJvs5Pu_6AsNoMgiT-CFsGrdENVoaDqa73IHNvTkmqwMPZDfWER_IGpo9wlyKVQNR0-RUMUntRP4zigBDMpoMwzu4n4aDqHkb24jbTSLkykxhSHkFo5JKopngavt3oVtr_W54DeoesRHvjJjT4lgcGlET9XysJkYtmNLH6sioS1qUx-rYqHTNrBc5aCFZjrCWFXXQihreeok29Y4Z0ku6ojOEzWNO5PMMzfjW7CkJfxBitd8mRbVYIjwnhTKrqqzn3GdkIckfC-Xmd74SFdcIu92uZ4sgvEFrhL2Of-5ddHpur931Ou3A6zroDeGW5_fOA8_1AtcNOj2_HWwd9G5zXeMPuoEfXLQ7vt_ze4GDaM60kMPmcLBnxPY3ZLg1AA?type=png"
      width="30%"
    />
  </a>
</p>
