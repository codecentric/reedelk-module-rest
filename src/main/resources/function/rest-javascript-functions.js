var MultipartBuilder = {};

(function(builder) {

    this.create = function(partName) {
        return builder.create(partName);
    };

}).call(MultipartBuilder, builder);
