var MultipartMessage = {};

(function(multipartMessageBuilderFactory) {

    this.part = function(partName) {
        return multipartMessageBuilderFactory.part(partName);
    };


}).call(MultipartMessage, multipartMessageBuilderFactory);
