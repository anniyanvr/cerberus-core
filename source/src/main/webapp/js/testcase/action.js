/*
 * Cerberus Copyright (C) 2013 - 2017 cerberustesting
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This file is part of Cerberus.
 *
 * Cerberus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Cerberus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Cerberus.  If not, see <http://www.gnu.org/licenses/>.
 */
var actionOptGroupList = [
        {"name":"access_application", "label":{"en":"Application Access","fr":"Accès à l'Application"}, "picto":"<img width='20px' height='20px' src='images/action-website.png'/>"},
        {"name":"mouse_action", "label":{"en":"Mouse Action","fr":"Action à la souris"}, "picto":"<img width='20px' height='20px' src='images/action-mouse.png'/>"},
        {"name":"finger_action", "label":{"en":"Finger Action","fr":"Action au doigt"}, "picto":"<img width='20px' height='20px' src='images/action-tap.png'/>"},
        {"name":"context_action", "label":{"en":"Context Action","fr":"Action de Contexte"}, "picto":"<img width='20px' height='20px' src='images/action-settings.png'/>"},
        {"name":"keyboard_action", "label":{"en":"Keyboard Action","fr":"Action au Clavier"}, "picto":"<img width='20px' height='20px' src='images/action-keyboard.png'/>"},
        {"name":"command", "label":{"en":"Execute Command","fr":"Execution de Commande"}, "picto":"<img width='20px' height='20px' src='images/action-command-line.png'/>"},
        {"name":"wait", "label":{"en":"Wait","fr":"Attendre"}, "picto":"<img width='20px' height='20px' src='images/action-time-left.png'/>"},
        {"name":"file", "label":{"en":"File","fr":"Fichier"}, "picto":"<img width='20px' height='20px' src='images/action-file.png'/>"},
        {"name":"context_control", "label":{"en":"Context Controls","fr":"Contexte des Contrôles"}, "picto":"<img width='20px' height='20px' src='images/action-share.png'/>"}

];

var actionOptList = {
        "unknown":{"group":"none","value":"Unknown","label_en": "None","label":{"en":"Define an action","fr":"Choisir une action"},"application_types":["GUI","SRV","IPA","APK","BAT","FAT","NONE"]},
        "click":{"group":"mouse_action","value":"click","label":{"en":"Click","fr":"Cliquer"},"application_types":["GUI","IPA","APK","FAT"],"field1":{"label":{"en": "Element path", "fr": "Chemin vers l'élement à cliquer"},"picto":"images/action-html.png","class": "col-lg-12"},"documentation":{"en":"...","fr":"..."}},
        "longPress":{"group":"mouse_action","value":"longPress","label":{"en":"longPress","fr":"Cliquer x secondes"},"application_types":["GUI","IPA","APK","FAT"], "field1":{"label": {"en":"Element path","fr":"Chemin vers l'élement à cliquer"},"picto":"images/action-html.png", "class": "col-lg-9"}, "field2":{"label":{"en":"[opt] Duration (ms) : 8000 by default","fr":"[opt] Valeur (ms) : 8000 par défaut"},"picto":"images/action-time-left.png", "class": "col-lg-3"},"documentation":{"en":"...","fr":"..."}},
        "mouseLeftButtonPress":{"group":"mouse_action","value": "mouseLeftButtonPress","label":{"en":"Press and keep left button","fr":"Presser et maintenir le bouton gauche"}, "application_types":["GUI","FAT"], "field1":{"label":{"en": "Element path", "fr": "Chemin vers l'élement à cibler"},"picto":"images/action-html.png", "class": "col-lg-12"}},
        "mouseLeftButtonRelease":{"group":"mouse_action","value": "mouseLeftButtonRelease","label":{"en":"Release left button","fr":"Relacher le bouton gauche"}, "application_types":["GUI","FAT"], "field1":{"label":{"en": "Element path", "fr": "Chemin vers l'élement"},"picto":"images/action-html.png", "class": "col-lg-12"}},
        "doubleClick":{"group":"mouse_action", "value": "doubleClick","label":{"en":"Double Click","fr":"Double Clic"}, "application_types":["GUI","FAT"], "field1":{"label":{"en": "Element path", "fr": "Chemin vers l'élement à double-cliquer"},"picto":"images/action-html.png", "class": "col-lg-12"}},
        "rightClick":{"group":"mouse_action", "value": "rightClick", "label":{"en":"Right Click","fr":"Clic droit"}, "application_types":["GUI","FAT"], "field1":{"label":{"en": "Element path", "fr": "Chemin vers l'élement à clicker avec le bouton droit"},"picto":"images/action-html.png", "class": "col-lg-12"}},
        "mouseOver":{"group":"mouse_action", "value": "mouseOver", "label":{"en":"Mouse Over","fr":"Souris sur l'élément"}, "application_types":["GUI","FAT"], "field1":{"label":{"en": "Element path", "fr": "Chemin vers l'élement"},"picto":"images/action-html.png", "class": "col-lg-12"}},
        "mouseMove":{"group":"mouse_action", "value": "mouseMove", "label":{"en":"Move Mouse","fr":"Déplacer la souris"}, "application_types":["GUI","FAT"], "field1":{"label":{"en": "Relative coord. (ex : 50,100 ; 200,50)", "fr": "Coordonnées relatives (ex : 50,100 ; 200,50)"}, "class": "col-lg-12"}},
        "openUrlWithBase":{"group":"access_application","value": "openUrlWithBase","label":{"en":"openUrlWithBase","fr":"Appeler l'URI"},"application_types":["GUI","IPA","APK"],"field1":{"label":{"en": "URI to call  (ex : /index.html)", "fr": "URI à appeler (ex : /index.html)"},"picto":"images/action-link.png", "class": "col-lg-12"}},
        "openUrlLogin":{"group":"access_application","value": "openUrlLogin","label":{"en":"openUrlLogin","fr":"Appeler l'URL de Login"},"application_types":["GUI","IPA","APK"]},
        "openUrl":{"group":"access_application","value": "openUrl","label":{"en":"Open Url","fr":"Appeler l'URL"},"application_types":["GUI","IPA","APK"],"field1":{"label":{"en": "URL to call (ex : http://www.domain.com)", "fr": "URL à appeler (ex : http://www.domain.com)"},"picto":"images/action-link.png", "class": "col-lg-12"}},
        "focusToIframe":{"group":"context_action","value": "focusToIframe","label":{"en":"Focus to Iframe","fr":"Switcher sur l'Iframe"},"application_types":["GUI"],"field1":{"label":{"en": "Element path of the target iFrame", "fr": "Chemin vers l'élement de l'iFrame à cibler"},"picto":"images/action-html.png", "class": "col-lg-12"}},
        "focusDefaultIframe":{"group":"context_action","value": "focusDefaultIframe","label":{"en":"Focus to main context","fr":"Switcher sur le context principal"},"application_types":["GUI"]},
        "switchToWindow":{"group":"context_action","value": "switchToWindow","label":{"en":"Switch to Window","fr":"Switcher sur l'onglet"},"application_types":["GUI"],"field1":{"label":{"en": "Window title or url", "fr": "Titre ou url de la fenêtre"},"picto":"images/action-website.png", "class": "col-lg-12"}},
        "manageDialog":{"group":"context_action","value": "manageDialog","label":{"en":"Manage Dialog","fr":"Gérer la popup"},"application_types":["GUI"],"field1":{"label":{"en": "ok or cancel", "fr": "ok ou cancel"},"picto":"images/action-font.png", "class": "col-lg-12"}},
        "manageDialogKeypress":{"group":"context_action","value": "manageDialogKeypress","label":{"en":"Manage Dialog pressing key","fr":"Switcher sur l'Iframe"},"application_types":["GUI"],"field1":{"label":{"en": "keys to press", "fr": "Touches à appuyer"},"picto":"images/action-keyboard.png", "class": "col-lg-12"}},
        "refreshCurrentPage":{"group":"context_action","value": "refreshCurrentPage","label":{"en":"Refresh Page","fr":"Recharger la page"},"application_types":["GUI"]},
        "executeJS":{"group":"command","value": "executeJS","label":{"en":"Execute Javascript Command","fr":"Executer une commande Javascript"},"application_types":["GUI"],"field1":{"label":{"en": "JavaScript to execute", "fr": "JavaScript à executer"},"picto":"images/action-command-line.png", "class": "col-lg-12"}},
        "executeCommand":{"group":"command","value":"executeCommand","label":{"en":"Execute Appium Command","fr":"Executer une commande Appium"},"application_types":["GUI","IPA","APK"], "field1":{"label": {"en":"Appium Command (ex : \"mobile:deepLink\")","fr":"Commande Appium (ex : \"mobile:deepLink\")"},"picto":"images/action-command-line.png", "class": "col-lg-6"}, "field2":{"label":{"en":"Arguments (ex : {url: \"www.site.com\", package: \"com.Package\"})","fr":"Arguments (ex : {url: \"www.site.com\", package: \"com.Package\"})"}, "class": "col-lg-6"},"documentation":{"en":"...","fr":"..."}},
        "executeCerberusCommand":{"group":"command","value": "executeCerberusCommand","label":{"en":"Execute Cerberus Command","fr":"Executer une commande Cerberus"},"application_types":["GUI"],"field1":{"label":{"en": "Command (ex : \"grep\")", "fr": "Commande (ex : \"grep\")"},"picto":"images/action-command-line.png", "class": "col-lg-12"}},
        "openApp":{"group":"access_application","value":"openApp","label":{"en":"Open Application","fr":"Lancer l'application"},"application_types":["GUI","IPA","APK","FAT"], "field1":{"label": {"en":"Application name or path or package for Android","fr":"Nom ou chemin de l'application, package pour android"},"picto":"images/action-mobile-application.png", "class": "col-lg-8"}, "field2":{"label":{"en":"[Optional, required for Android] Activity","fr":"[Optionnel, obligatoire pour Android] Activity"}, "class": "col-lg-4"},"documentation":{"en":"...","fr":"..."}},
        "closeApp":{"group":"command","value": "closeApp","label":{"en":"Close Application","fr":"Fermer l'application"},"application_types":["GUI","IPA","APK","FAT"],"field1":{"label":{"en": "Application name or path", "fr": "Nom ou chemin de l'application"},"picto":"images/action-mobile-application.png", "class": "col-lg-12"}},
        "dragAndDrop":{"group":"mouse_action","value": "dragAndDrop","label":{"en":"Drag And Drop","fr":"Glisser Déposer"},"application_types":["GUI","FAT"],"field1":{"label":{"en": "Element path", "fr": "Chemin de l'élement"},"picto":"images/action-html.png", "class": "col-lg-12"}, "field2":{"label":{"en":"Destination Element Path","fr":"Destination de l'élément"},"picto":"images/action-html.png", "class": "col-lg-12"},"documentation":{"en":"...","fr":"..."}},
        "select":{"group":"mouse_action","value": "select","label":{"en":"Choose option in select box","fr":"Choisir une option dans un Select"},"application_types":["GUI"],"field1":{"label":{"en": "Element path", "fr": "Chemin vers l'élement"},"picto":"images/action-html.png", "class": "col-lg-12"}, "field2":{"label":{"en":"Option value","fr":"Chemin vers l'option"},"picto":"images/action-command-line.png", "class": "col-lg-12"},"documentation":{"en":"...","fr":"..."}},
        "keypress":{"group":"keyboard_action","value": "keypress","label":{"en":"Press Key","fr":"Appuyer sur une touche"},"application_types":["GUI"],"field1":{"label":{"en": "[opt] Target element path", "fr": "[opt] Chemin vers l'élement à cibler"},"picto":"images/action-html.png", "class": "col-lg-12"}, "field2":{"label":{"en":"Key to Press","fr":"Touche à appuyer"},"picto":"images/action-keyboard.png", "class": "col-lg-6"}, "field3":{"label":{"en":"[opt] Modifier to press","fr":"[opt] Touche modificatrice"},"picto":"images/action-keyboard.png", "class": "col-lg-6"},"documentation":{"en":"...","fr":"..."}},
        "type":{"group":"keyboard_action","value": "type","label":{"en":"Feed field","fr":"Remplir le champs"},"application_types":["GUI","APK","IPA","FAT"],"field1":{"label":{"en": "Element path", "fr": "Chemin vers l'élement"},"picto":"images/action-html.png", "class": "col-lg-12"}, "field2":{"label":{"en":"Text to Type","fr":"Texte à entrer"},"picto":"images/action-font.png", "class": "col-lg-12"},"documentation":{"en":"...","fr":"..."}},
        "hideKeyboard":{"group":"keyboard_action","value": "hideKeyboard","label":{"en":"Hide Keyboard","fr":"Cacher le Clavier"},"application_types":["APK","IPA"],"documentation":{"en":"...","fr":"..."}},
        "clearField":{"group":"keyboard_action","value": "clearField","label":{"en":"Clear Field","fr":"Vider l'élément"},"application_types":["GUI","APK","IPA","FAT"],"field1":{"label":{"en": "Element path", "fr": "Chemin vers l'élement à effacer"},"picto":"images/action-html.png", "class": "col-lg-12"},"documentation":{"en":"...","fr":"..."}},
        "swipe":{"group":"finger_action","value": "swipe","label":{"en":"Swipe","fr":"Swiper"},"application_types":["APK","IPA"],"field1":{"label":{"en": "Action (UP DOWN LEFT RIGHT CUSTOM...)", "fr": "Action (UP DOWN LEFT RIGHT CUSTOM...)"},"picto":"images/action-font.png ", "class": "col-lg-6"}, "field2":{"label":{"en":"Direction x;y;z;y","fr":"Direction x;y;z;y"},"picto":"images/action-settings.png", "class": "col-lg-6"},"documentation":{"en":"...","fr":"..."}},
        "wait":{"group":"wait","value": "wait","label":{"en":"Wait","fr":"Attendre"},"application_types":["GUI","SRV","IPA","APK","BAT","FAT","NONE"],"field1":{"label":{"en": "Duration(ms) or Element", "fr": "Valeur (ms) ou élement"},"picto":"images/action-time-left.png", "class": "col-lg-12"},"documentation":{"en":"...","fr":"..."}},
        "waitVanish":{"group":"wait","value": "waitVanish","label":{"en":"Wait Element Vanish","fr":"Attendre la disparition de l'élément"},"application_types":["GUI","SRV","IPA","APK","FAT"],"field1":{"label":{"en": "Element path", "fr": "Element"},"picto":"images/action-html.png", "class": "col-lg-12"},"documentation":{"en":"...","fr":"..."}},
        "waitNetworkTrafficIdle":{"group":"wait","value": "waitNetworkTrafficIdle","label":{"en":"Wait Network traffic Idle","fr":"Attendre la fin du chargement réseau"},"application_types":["GUI","SRV","IPA","APK","BAT","FAT","NONE"],"documentation":{"en":"...","fr":"..."}},
        "callService":{"group":"access_application","value": "callService","label":{"en":"Call Service","fr":"Appeler le Service"},"application_types":["GUI","APK","IPA","FAT","SRV"],"field1":{"label":{"en": "Service Name", "fr": "Nom du Service"},"picto":"images/action-api.png", "class": "col-lg-12"}, "field2":{"label":{"en":"Nb Evt (Kafka)","fr":"Nb Evt à attendre (Kafka)"},"picto":"images/action-settings.png", "class": "col-lg-6"}, "field3":{"label":{"en":"Evt Wait sec (Kafka)","fr":"Tps d'attente en sec (Kafka)"},"picto":"images/action-time-left.png", "class": "col-lg-6"},"documentation":{"en":"...","fr":"..."}},
        "executeSqlUpdate":{"group":"command","value": "executeSqlUpdate","label":{"en":"Execute SQL script (insert/update)","fr":"Executer un script SQL (insert/update)"},"application_types":["GUI","SRV","IPA","APK","FAT"],"field1":{"label":{"en": "Database Name", "fr": "Nom de Base de donnée"},"picto":"images/action-font.png", "class": "col-lg-8"},"field2":{"label":{"en": "Script", "fr": "Script à executer"},"picto":"images/action-script.png", "class": "col-lg-12"},"documentation":{"en":"...","fr":"..."}},
        "executeSqlStoredProcedure":{"group":"command","value": "executeSqlStoredProcedure","label":{"en":"Execute SQL Stored Procedure","fr":"Executer une procedure stoquée SQL"},"application_types":["GUI","SRV","IPA","APK","FAT"],"field1":{"label":{"en": "Database Name", "fr": "Nom de Base de donnée"},"picto":"images/action-font.png", "class": "col-lg-8"},"field2":{"label":{"en": "Stored Procedure", "fr": "Procedure Stoquée à executer"},"picto":"images/action-script.png", "class": "col-lg-12"},"documentation":{"en":"...","fr":"..."}},
        "calculateProperty":{"group":"command","value": "calculateProperty","label":{"en":"Calculate Property","fr":"Calculer la propriété"},"application_types":["GUI","SRV","IPA","APK","FAT"],"field1":{"label":{"en": "Property Name", "fr": "Nom d'une Proprieté"},"picto":"images/action-font.png", "class": "col-lg-6"},"field2":{"label":{"en": "[opt] Name of an other property", "fr": "[opt] Nom d'une autre propriété"},"picto":"images/action-font.png", "class": "col-lg-6"},"documentation":{"en":"...","fr":"..."}},
        "setNetworkTrafficContent":{"group":"context_control","value": "setNetworkTrafficContent","label":{"en":"Switch context to network traffic content","fr":"Passer au contenu du traffic réseau"},"application_types":["GUI","SRV","IPA","APK","FAT"],"field1":{"label":{"en": "url to filter", "fr": "URL à filtrer"},"picto":"images/action-link.png", "class": "col-lg-8"},"field2":{"label":{"en": "Activate http response content (Y/N)", "fr": "Activation du contenu des reponses http (Y/N)"},"picto":"images/action-settings.png", "class": "col-lg-4"},"documentation":{"en":"...","fr":"..."}},
        "indexNetworkTraffic":{"group":"command","value": "indexNetworkTraffic","label":{"en":"Index Network Traffic","fr":"Indexer le contenu du traffic réseau"},"application_types":["GUI","SRV","IPA","APK","FAT"],"field1":{"label":{"en": "[opt] Index name", "fr": "[opt] Nom de l'index"},"picto":"images/action-font.png", "class": "col-lg-12"},"documentation":{"en":"...","fr":"..."}},
        "setServiceCallContent":{"group":"context_control","value": "setServiceCallContent","label":{"en":"Switch context to service call content","fr":"Passer au contenu du dernier service appelé"},"application_types":["GUI","SRV","IPA","APK","FAT"],"documentation":{"en":"...","fr":"..."}},
        "setConsoleContent":{"group":"context_control","value": "setConsoleContent","label":{"en":"Switch context to web console content","fr":"Passer au contenu de la console"},"application_types":["GUI","SRV","IPA","APK","FAT"],"documentation":{"en":"...","fr":"..."}},
        "setContent":{"group":"context_control","value": "setContent","label":{"en":"Switch context to specific content","fr":"Passer au contenu spécifique"},"application_types":["GUI","SRV","IPA","APK","FAT"],"field1":{"label":{"en": "Value to Set", "fr": "Valeur"},"picto":"images/action-font.png", "class": "col-lg-12"},"documentation":{"en":"...","fr":"..."}},
        "scrollTo":{"group":"mouse_action","value": "scrollTo","label":{"en":"Scroll to element","fr":"Scroller jusqu'à l'élément"},"application_types":["GUI","IPA","APK","FAT"],"field1":{"label":{"en": "element ('id=ressource-id'. Empty if you want use text)", "fr": "element (id, xpath, ..., et text=)"},"picto":"images/action-html.png", "class": "col-lg-12"},"field2":{"label":{"en": "text (empty if you want use element)", "fr": "Nombre maximum de scroll vers le bas (8 par defaut)"}, "class": "col-lg-12"},"documentation":{"en":"...","fr":"..."}},
        "installApp":{"group":"access_application","value": "installApp","label":{"en":"Install Application","fr":"Installer l'Application"},"application_types":["IPA","APK"],"field1":{"label":{"en": "Application path (ex : /root/toto.apk)", "fr": "Chemin vers l'application (ex : /root/toto.apk)"},"picto":"images/action-mobile-application.png", "class": "col-lg-12"},"documentation":{"en":"...","fr":"..."}},
        "removeApp":{"group":"access_application","value": "removeApp","label":{"en":"Uninstall Application","fr":"Désinstaller l'Application"},"application_types":["IPA","APK"],"field1":{"label":{"en": "Application package (ex : com.cerberus.appmobile)", "fr": "Package de l'application (ex : com.cerberus.appmobile)"},"picto":"images/action-mobile-application.png", "class": "col-lg-12"},"documentation":{"en":"...","fr":"..."}},
        "cleanRobotFile":{"group":"file","value": "cleanRobotFile","label":{"en":"Clean Robot File Folder","fr":"Vider le dossier fichier du robot"},"application_types":["GUI","FAT"],"field1":{"label":{"en": "Path/Pattern to empty. ex : /home/seluser/Downloads/", "fr": "Chemin du dossier à vider. ex : /home/seluser/Downloads/"},"picto":"images/action-file.png", "class": "col-lg-12"},"documentation":{"en":"...","fr":"..."}},
        "uploadRobotFile":{"group":"file","value": "uploadRobotFile","label":{"en":"Upload File to Robot","fr":"Upload un fichier vers le Robot"},"application_types":["GUI","APK","IPA","FAT","SRV"],"field1":{"label":{"en": "Filename to create. ex : /home/seluser/Downloads/test.json", "fr": "Nom du fichier à créer. ex : /home/seluser/Downloads/test.json"},"picto":"images/action-file.png", "class": "col-lg-12"}, "field2":{"label":{"en":"Content to upload (base64)","fr":"Contenu à charger (base64)"},"picto":"images/action-font.png", "class": "col-lg-12"}, "field3":{"label":{"en":"Option (EMPTYFOLDER)","fr":"Option (EMPTYFOLDER)"},"picto":"images/action-settings.png", "class": "col-lg-12"},"documentation":{"en":"...","fr":"..."}},
        "getRobotFile":{"group":"file","value": "getRobotFile","label":{"en":"Download File from Robot","fr":"Télécharger un fichier depuis le Robot"},"application_types":["GUI","APK","IPA","FAT","SRV"],"field1":{"label":{"en": "Path/Pattern to retrieved. ex : /home/seluser/Downloads/", "fr": "Nom du fichier à récupérer. ex : /home/seluser/Downloads/"},"picto":"images/action-file.png", "class": "col-lg-12"}, "field2":{"label":{"en":"Nb of files","fr":"Nb de fichiers"},"picto":"images/action-numeric.png", "class": "col-lg-6"}, "field3":{"label":{"en":"Sorting Option. (LASTMODIFIED/IGNORECASEDESC/IGNORECASEASC/DESC/ASC)","fr":"Option de tri. (LASTMODIFIED/IGNORECASEDESC/IGNORECASEASC/DESC/ASC)"},"picto":"images/action-settings.png", "class": "col-lg-6"},"documentation":{"en":"...","fr":"..."}},
        "doNothing":{"group":"none","value": "doNothing","label":{"en":"No action","fr":"Pas d'action"},"application_types":["GUI","SRV","IPA","APK","FAT"],"documentation":{"en":"...","fr":"..."}}
};
