$(function () {

  $('.analyse-bttn').click(function() {
  $("#analytics").html('');
    $('#msg-block').hide();
    var url = $('.url-block').val();

    if(!url) {
      alert('Please provide the website url to analyse HTML.');
      return;
    }

    analyse(url);

  });

  var analyse = function(url) {
    showLoader();
    var request = $.ajax({
        url: '/v1/html/analyse?url=' + url,
        method: "GET",
        dataType: "json"
      });

      request.done(function( data ) {
        renderData(data);
        hideLoader();
      });

      request.fail(function( jqXHR, textStatus ) {
        hideLoader();
        var errMsg = JSON.parse(jqXHR.responseText);
        alert(errMsg.errorMessage);
      });
  };

  var renderData = function(data) {
    if(!data) {
      error('There is no data to show.');
      return;
    }

    $('#analytics').append(renderBasicInfo(data));
    $('#analytics').append(renderHeadings(data.headings));
    $('#analytics').append(renderHyperlinks(data.hyperlinks));
  };

  var renderBasicInfo = function(data) {
    var $div = $("<div>", {id: "basicInfo", "class": "row"});
    var html = '<label>Basic Information</label>'+
                '<table class="table">'+
                  '<thead>'+
                  '<tr>'+
                    '<th scope="col">HTML Version</th>'+
                    '<th scope="col">Title</th>'+
                    '<th scope="col">Has Login Box</th>'+
                  '</tr>'+
                  '</thead>'+
                  '<tbody>'+
                  '<tr>'+
                    '<td>' + data.version + '</td>'+
                    '<td>' + data.title + '</td>'+
                    '<td>' + data.loginPage + '</td>'+
                  '</tr>'+
                  '</tbody>'+
                '</table>';
    $div.append(html);
    $("#analytics").append($div);
  };

  var renderHeadings = function(headings) {
    var $div = $("<div>", {id: "headings", "class": "row"});
    var html = '<label>Headings</label>'+
      '<table class="table">'+
        '<thead>'+
        '<tr>'+
          '<th scope="col">Heading Level</th>'+
          '<th scope="col">Count</th>'+
        '</tr>'+
        '</thead>'+
        '<tbody>';

    if(headings) {
      $.each(headings, function(headingLevel, texts){
        html += '<tr>'+
          '<td>'+ headingLevel +'</td>'+
          '<td>'+ texts.length +'</td>'+
        '</tr>';
      });
    }

    html += '</tbody></table>';

    $div.append(html);
    $("#analytics").append($div);
  };

  var renderHyperlinks = function(hyperlinks) {
    var $div = $("<div>", {id: "hyperlinks", "class": "row"});
    var html = '<label>Unique Hyperlinks on the page</label>'+
      '<table class="table">'+
        '<thead>'+
        '<tr>'+
          '<th class="text-left" scope="col" style="width: 60%">Link</th>'+
          '<th scope="col" style="width: 10%">Type</th>'+
          '<th scope="col" style="width: 10%">Reachable</th>'+
          '<th scope="col" style="width: 20%">Redirection Remark</th>'+
        '</tr>'+
        '</thead>'+
        '<tbody>';

    if(hyperlinks) {
      $.each(hyperlinks, function(type, links){
        if(links && links.length > 1) {
          $.each(links, function(index, link){
            var shortLength = 60;
            var shortened = (link.url.length > shortLength ? link.url.substring(0, shortLength) + '....' : link.url);

            html += '<tr>'+
                    '<td class="text-left"><a target="_blank"" href="'+ link.url +'">'+ shortened +'</a></td>'+
                    '<td>'+ link.type +'</td>'+
                    '<td>'+ link.reachable +'</td>'+
                    '<td>'+ (link.remark ? link.remark : '') +'</td>'+
                  '</tr>';
          });
        }
      });
    }

    html += '</tbody></table>';

    $div.append(html);
    $("#analytics").append($div);
  };

  var showLoader = function() {
    $('#pleaseWaitDialog').modal();
  };

  var hideLoader = function() {
    $('#pleaseWaitDialog').modal('hide');
  };
});
