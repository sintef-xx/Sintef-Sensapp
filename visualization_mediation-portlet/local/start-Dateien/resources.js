
// Provide a default path to dwr.engine
if (typeof this['dwr'] == 'undefined') this.dwr = {};
if (typeof dwr['engine'] == 'undefined') dwr.engine = {};

if (typeof this['resources'] == 'undefined') this.resources = {};

resources._path = '/ResourceManager-0.0.1-SNAPSHOT/dwr';

resources.getState = function(p0, callback) {
  dwr.engine._execute(resources._path, 'resources', 'getState', p0, callback);
};

resources.refresh = function(callback) {
  dwr.engine._execute(resources._path, 'resources', 'refresh', callback);
};

resources.listServices = function(callback) {
  dwr.engine._execute(resources._path, 'resources', 'listServices', callback);
};

resources.getOGCLink = function(p0, callback) {
  dwr.engine._execute(resources._path, 'resources', 'getOGCLink', p0, callback);
};

resources.getWSLLink = function(p0, callback) {
  dwr.engine._execute(resources._path, 'resources', 'getWSLLink', p0, callback);
};


