//彩云天气接口转发服务 需要部署到 cloudflare 的 Workers  服务中使用

export default {
    async fetch(re, en, ct) {

        //设置头部信息
        const corshead = {
            'Access-Control-Allow-Origin': '*', 'Access-Control-Allow-Methods': 'GET, OPTIONS', 'Access-Control-Allow-Headers': 'Content-Type',
        };

        //处理预检请求
        if (re.method === 'OPTIONS') {
            return new Response(null, {
                headers: corshead,
            });
        }

        try {
            //获取原始请求链接
            const original = new URL(re.url);

            //构建目标请求链接
            const purposes = new URL(re.url.replace(original.origin, 'https://api.caiyunapp.com'));

            //复制所有查询参数
            original.searchParams.forEach((item, keys) => {
                purposes.searchParams.set(keys, item);
            });

            //创建新的请求对象
            const requests = new Request(purposes.toString(), {
                headers: re.headers, method: re.method, body: re.body,
            });

            //转发请求
            const transmit = await fetch(requests);

            //创建响应
            const response = new Response(transmit.body, transmit);

            //添加头部
            Object.entries(corshead).forEach(([keys, item]) => {
                response.headers.set(keys, item);
            });

            return response;
        } catch (erro) {
            return new Response(JSON.stringify({
                errors: erro.message, status: 'proxy_error'
            }), {
                status: 500, headers: {
                    'Content-Type': 'application/json', ...corshead
                }
            });
        }
    },
};